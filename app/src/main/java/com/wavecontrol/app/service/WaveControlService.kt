package com.wavecontrol.app.service

import android.Manifest
import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.view.Surface
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.wavecontrol.app.MainActivity
import com.wavecontrol.app.R
import com.wavecontrol.app.detector.CameraWaveDetector
import com.wavecontrol.app.detector.DetectionEvent
import com.wavecontrol.app.detector.ProximityWaveDetector
import com.wavecontrol.app.model.WaveAction
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.model.WaveSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WaveControlService : Service(), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle get() = lifecycleRegistry

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var settingsRepository: WaveSettingsRepository
    private lateinit var actionExecutor: WaveActionExecutor
    private lateinit var overlayManager: FloatingOverlayManager

    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraDetector: CameraWaveDetector? = null
    private var proximityDetector: ProximityWaveDetector? = null
    private var cameraExecutor: ExecutorService? = null

    private var currentSettings: WaveSettings = WaveSettings()
    private var isDetectionPaused = false

    // Screen State Receiver to pause camera and sensors when screen is off, sleeping, or locked
    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF, Intent.ACTION_DREAMING_STARTED -> {
                    pauseDetection("Screen Off / Sleep Mode")
                }
                Intent.ACTION_SCREEN_ON -> {
                    if (isDeviceActive()) {
                        resumeDetection()
                    } else {
                        pauseDetection("Device Locked")
                    }
                }
                Intent.ACTION_USER_PRESENT, Intent.ACTION_DREAMING_STOPPED -> {
                    if (isDeviceActive()) {
                        resumeDetection()
                    }
                }
            }
        }
    }

    private fun isDeviceActive(): Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager

        val isInteractive = powerManager?.isInteractive ?: false
        val isLocked = keyguardManager?.isKeyguardLocked ?: false

        return isInteractive && !isLocked
    }

    override fun onCreate() {
        super.onCreate()
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        instance = this

        settingsRepository = WaveSettingsRepository.getInstance(this)
        currentSettings = settingsRepository.getSettings()

        actionExecutor = WaveActionExecutor(this)
        overlayManager = FloatingOverlayManager(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        createNotificationChannel()

        // Register screen state receiver for screen off, lockscreen, and dreaming/sleep events
        val screenFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
            addAction(Intent.ACTION_DREAMING_STARTED)
            addAction(Intent.ACTION_DREAMING_STOPPED)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenStateReceiver, screenFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(screenStateReceiver, screenFilter)
        }

        // Listen for settings changes
        settingsRepository.settingsFlow
            .onEach { settings ->
                val prev = currentSettings
                currentSettings = settings
                cameraDetector?.updateConfig(
                    sensitivity = settings.sensitivity,
                    cooldownMs = settings.cooldownMs,
                    swapAxes = settings.swapAxes,
                    invertHorizontal = settings.invertHorizontal,
                    invertVertical = settings.invertVertical
                )
                proximityDetector?.updateCooldown(settings.cooldownMs)

                if (prev.isProximityWaveEnabled != settings.isProximityWaveEnabled) {
                    if (settings.isProximityWaveEnabled && !isDetectionPaused && isDeviceActive()) {
                        proximityDetector?.start()
                    } else {
                        proximityDetector?.stop()
                    }
                }

                if (prev.isCameraWaveEnabled != settings.isCameraWaveEnabled) {
                    if (settings.isCameraWaveEnabled && !isDetectionPaused && isDeviceActive()) {
                        bindCameraUseCases()
                    } else {
                        cameraProvider?.unbindAll()
                    }
                }

                if (!settings.isEnabled) {
                    stopSelf()
                }
            }
            .launchIn(serviceScope)

        startForegroundServiceWithNotification()

        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED

        setupDetectors()

        // If screen is turned off or locked at launch time, pause immediately
        if (!isDeviceActive()) {
            pauseDetection("Screen Off / Locked")
        }
    }

    private fun setupDetectors() {
        // Proximity detector
        proximityDetector = ProximityWaveDetector(
            context = this,
            cooldownMs = currentSettings.cooldownMs,
            onGestureDetected = { event ->
                handleGesture(event)
            }
        )
        if (currentSettings.isProximityWaveEnabled && !isDetectionPaused && isDeviceActive()) {
            proximityDetector?.start()
        }

        // Camera detector
        if (currentSettings.isCameraWaveEnabled) {
            setupCameraDetection()
        }
    }

    private fun setupCameraDetection() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                if (!isDetectionPaused && currentSettings.isCameraWaveEnabled && isDeviceActive()) {
                    bindCameraUseCases()
                }
            } catch (e: Exception) {
                Log.e("WaveControlService", "Error setting up ProcessCameraProvider: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val provider = cameraProvider ?: run {
            setupCameraDetection()
            return
        }
        val executor = cameraExecutor ?: return

        // Verify camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.w("WaveControlService", "Camera permission not granted yet, cannot bind CameraX.")
            return
        }

        try {
            cameraDetector = CameraWaveDetector(
                sensitivity = currentSettings.sensitivity,
                cooldownMs = currentSettings.cooldownMs,
                swapAxes = currentSettings.swapAxes,
                invertHorizontal = currentSettings.invertHorizontal,
                invertVertical = currentSettings.invertVertical,
                onGestureDetected = { event ->
                    handleGesture(event)
                },
                onLiveMotion = { energy, vx, vy ->
                    _liveMotionFlow.tryEmit(Triple(energy, vx, vy))
                }
            )

            val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            @Suppress("DEPRECATION")
            val displayRotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    display?.rotation ?: Surface.ROTATION_0
                } catch (_: Exception) {
                    Surface.ROTATION_0
                }
            } else {
                windowManager?.defaultDisplay?.rotation ?: Surface.ROTATION_0
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetRotation(displayRotation)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(executor, cameraDetector!!)

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            provider.unbindAll()
            provider.bindToLifecycle(
                this,
                cameraSelector,
                imageAnalysis
            )
            Log.d("WaveControlService", "CameraX successfully bound for wave detection!")
        } catch (e: Exception) {
            Log.e("WaveControlService", "Error binding camera use cases: ${e.message}", e)
        }
    }

    private fun pauseDetection(reason: String) {
        if (isDetectionPaused) return
        isDetectionPaused = true
        try {
            cameraProvider?.unbindAll()
            proximityDetector?.stop()
            updateNotification("Wave Control Paused", "Sensors inactive ($reason)")
        } catch (e: Exception) {
            Log.w("WaveControlService", "Error pausing detection: ${e.message}")
        }
    }

    private fun resumeDetection() {
        if (!currentSettings.isEnabled) return
        if (!isDeviceActive()) {
            Log.d("WaveControlService", "Device is locked or non-interactive, skipping resume.")
            return
        }
        if (!isDetectionPaused) return
        isDetectionPaused = false
        try {
            if (currentSettings.isCameraWaveEnabled) {
                bindCameraUseCases()
            }
            if (currentSettings.isProximityWaveEnabled) {
                proximityDetector?.start()
            }
            updateNotification("Wave Control Running", "Detecting hand gestures and proximity sensor...")
        } catch (e: Exception) {
            Log.w("WaveControlService", "Error resuming detection: ${e.message}")
        }
    }

    private fun handleGesture(event: DetectionEvent) {
        if (!currentSettings.isEnabled || isDetectionPaused || !isDeviceActive()) return

        try {
            val action = currentSettings.getActionForGesture(event.gesture)
            if (action == WaveAction.NONE) return

            // Execute action
            actionExecutor.execute(action, currentSettings)

            // Show floating HUD feedback if enabled
            if (currentSettings.isFloatingHudEnabled) {
                overlayManager.showGestureFeedback(event.gesture, action)
            }

            // Notify UI subscribers
            _lastGestureFlow.tryEmit(event)
        } catch (e: Exception) {
            Log.e("WaveControlService", "Error handling gesture ${event.gesture}: ${e.message}", e)
        }
    }

    private fun startForegroundServiceWithNotification() {
        val notification = buildNotification("Wave Control Running", "Detecting hand gestures and proximity sensor...")

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val fgsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
                } else {
                    0
                }
                startForeground(NOTIFICATION_ID, notification, fgsType)
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Throwable) {
            Log.w("WaveControlService", "Failed to startForeground with type: ${e.message}. Falling back.")
            try {
                startForeground(NOTIFICATION_ID, notification)
            } catch (fallbackError: Throwable) {
                Log.e("WaveControlService", "Failed to startForeground fallback: ${fallbackError.message}")
            }
        }
    }

    private fun updateNotification(title: String, text: String) {
        try {
            val notification = buildNotification(title, text)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.notify(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.w("WaveControlService", "Failed to update notification: ${e.message}")
        }
    }

    private fun buildNotification(title: String, text: String): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopIntent = Intent(this, WaveControlService::class.java).apply {
            action = ACTION_STOP_SERVICE
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .addAction(0, "Turn Off", stopPendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_desc)
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_SERVICE -> {
                settingsRepository.updateSettings { it.copy(isEnabled = false) }
                stopSelf()
                return START_NOT_STICKY
            }
            ACTION_REBIND_CAMERA -> {
                if (cameraProvider == null) {
                    setupCameraDetection()
                } else if (!isDetectionPaused && currentSettings.isCameraWaveEnabled && isDeviceActive()) {
                    bindCameraUseCases()
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        try {
            unregisterReceiver(screenStateReceiver)
        } catch (_: Exception) {}

        try {
            cameraProvider?.unbindAll()
        } catch (_: Exception) {}

        proximityDetector?.stop()
        actionExecutor.release()
        overlayManager.removeOverlay()

        cameraExecutor?.shutdown()
        cameraExecutor = null

        serviceJob.cancel()
        instance = null
        super.onDestroy()
    }

    companion object {
        const val CHANNEL_ID = "wave_control_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_STOP_SERVICE = "com.wavecontrol.app.action.STOP"
        const val ACTION_REBIND_CAMERA = "com.wavecontrol.app.action.REBIND_CAMERA"

        var instance: WaveControlService? = null
            private set

        val isRunning: Boolean
            get() = instance != null

        private val _lastGestureFlow = MutableSharedFlow<DetectionEvent>(replay = 1, extraBufferCapacity = 10)
        val lastGestureFlow = _lastGestureFlow.asSharedFlow()

        private val _liveMotionFlow = MutableSharedFlow<Triple<Float, Float, Float>>(replay = 1, extraBufferCapacity = 1)
        val liveMotionFlow = _liveMotionFlow.asSharedFlow()

        fun notifyCameraPermissionGranted(context: Context) {
            try {
                instance?.let { service ->
                    if (service.cameraProvider == null) {
                        service.setupCameraDetection()
                    } else if (!service.isDetectionPaused && service.currentSettings.isCameraWaveEnabled && service.isDeviceActive()) {
                        service.bindCameraUseCases()
                    }
                } ?: run {
                    val intent = Intent(context, WaveControlService::class.java).apply {
                        action = ACTION_REBIND_CAMERA
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent)
                    } else {
                        context.startService(intent)
                    }
                }
            } catch (e: Exception) {
                Log.w("WaveControlService", "Error notifying camera permission: ${e.message}")
            }
        }

        fun startService(context: Context) {
            try {
                val intent = Intent(context, WaveControlService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (e: Exception) {
                Log.e("WaveControlService", "Failed to start service: ${e.message}", e)
            }
        }

        fun stopService(context: Context) {
            try {
                val intent = Intent(context, WaveControlService::class.java)
                context.stopService(intent)
            } catch (e: Exception) {
                Log.e("WaveControlService", "Failed to stop service: ${e.message}", e)
            }
        }
    }
}
