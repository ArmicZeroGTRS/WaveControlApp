package com.wavecontrol.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
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
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.model.WaveSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.Executors

class WaveControlService : Service(), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle get() = lifecycleRegistry

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var settingsRepository: WaveSettingsRepository
    private lateinit var actionExecutor: WaveActionExecutor
    private lateinit var overlayManager: FloatingOverlayManager

    private var cameraDetector: CameraWaveDetector? = null
    private var proximityDetector: ProximityWaveDetector? = null
    private var cameraExecutor = Executors.newSingleThreadExecutor()

    private var currentSettings: WaveSettings = WaveSettings()

    override fun onCreate() {
        super.onCreate()
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        instance = this

        settingsRepository = WaveSettingsRepository.getInstance(this)
        currentSettings = settingsRepository.getSettings()

        actionExecutor = WaveActionExecutor(this)
        overlayManager = FloatingOverlayManager(this)

        createNotificationChannel()

        // Listen for settings changes
        settingsRepository.settingsFlow
            .onEach { settings ->
                val prev = currentSettings
                currentSettings = settings
                cameraDetector?.updateConfig(settings.sensitivity, settings.cooldownMs)
                proximityDetector?.updateCooldown(settings.cooldownMs)

                if (prev.isProximityWaveEnabled != settings.isProximityWaveEnabled) {
                    if (settings.isProximityWaveEnabled) {
                        proximityDetector?.start()
                    } else {
                        proximityDetector?.stop()
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
        if (currentSettings.isProximityWaveEnabled) {
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
                val cameraProvider = cameraProviderFuture.get()

                cameraDetector = CameraWaveDetector(
                    sensitivity = currentSettings.sensitivity,
                    cooldownMs = currentSettings.cooldownMs,
                    onGestureDetected = { event ->
                        handleGesture(event)
                    },
                    onLiveMotion = { energy, vx, vy ->
                        _liveMotionFlow.tryEmit(Triple(energy, vx, vy))
                    }
                )

                val imageAnalysis = ImageAnalysis.Builder()
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(cameraExecutor, cameraDetector!!)

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    imageAnalysis
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun handleGesture(event: DetectionEvent) {
        if (!currentSettings.isEnabled) return

        val action = currentSettings.getActionForGesture(event.gesture)

        // Execute action
        actionExecutor.execute(action, currentSettings)

        // Show floating HUD feedback if enabled
        if (currentSettings.isFloatingHudEnabled) {
            overlayManager.showGestureFeedback(event.gesture, action)
        }

        // Notify UI subscribers
        _lastGestureFlow.tryEmit(event)
    }

    private fun startForegroundServiceWithNotification() {
        val notification = buildNotification("Wave Control Berjalan", "Mendeteksi gerakan tangan dan sensor jarak...")

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
            .addAction(0, "Matikan", stopPendingIntent)
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
        if (intent?.action == ACTION_STOP_SERVICE) {
            settingsRepository.updateSettings { it.copy(isEnabled = false) }
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        proximityDetector?.stop()
        actionExecutor.release()
        overlayManager.removeOverlay()
        cameraExecutor.shutdown()
        serviceJob.cancel()
        instance = null
        super.onDestroy()
    }

    companion object {
        const val CHANNEL_ID = "wave_control_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_STOP_SERVICE = "com.wavecontrol.app.action.STOP"

        var instance: WaveControlService? = null
            private set

        val isRunning: Boolean
            get() = instance != null

        private val _lastGestureFlow = MutableSharedFlow<DetectionEvent>(replay = 1, extraBufferCapacity = 10)
        val lastGestureFlow = _lastGestureFlow.asSharedFlow()

        private val _liveMotionFlow = MutableSharedFlow<Triple<Float, Float, Float>>(replay = 1, extraBufferCapacity = 1)
        val liveMotionFlow = _liveMotionFlow.asSharedFlow()

        fun startService(context: Context) {
            val intent = Intent(context, WaveControlService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, WaveControlService::class.java)
            context.stopService(intent)
        }
    }
}
