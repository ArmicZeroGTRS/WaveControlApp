package com.wavecontrol.app.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import com.wavecontrol.app.accessibility.WaveAccessibilityService
import com.wavecontrol.app.detector.DetectionEvent
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveAction
import com.wavecontrol.app.model.WaveSettingsRepository
import com.wavecontrol.app.service.WaveActionExecutor
import com.wavecontrol.app.service.WaveControlService
import com.wavecontrol.app.ui.screens.GestureConfigScreen
import com.wavecontrol.app.ui.screens.GuideScreen
import com.wavecontrol.app.ui.screens.HomeScreen
import com.wavecontrol.app.ui.screens.SettingsScreen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val context = LocalContext.current
    val repository = remember { WaveSettingsRepository.getInstance(context) }
    val settings by repository.settingsFlow.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var lastEvent by remember { mutableStateOf<DetectionEvent?>(null) }

    // Dynamic Permission States
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    var isAccessibilityEnabled by remember {
        mutableStateOf(WaveAccessibilityService.isAccessibilityEnabled(context))
    }

    var canDrawOverlays by remember {
        mutableStateOf(Settings.canDrawOverlays(context))
    }

    // Permission Launchers
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            Toast.makeText(context, "Izin kamera diperlukan untuk mendeteksi gerakan!", Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotificationPermission = granted
    }

    // Recheck permissions on resume
    DisposableEffect(Unit) {
        val listener = {
            hasCameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                hasNotificationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            }
            isAccessibilityEnabled = WaveAccessibilityService.isAccessibilityEnabled(context)
            canDrawOverlays = Settings.canDrawOverlays(context)
        }
        listener.invoke()
        onDispose { }
    }

    // Collect detected gestures from Service
    LaunchedEffect(Unit) {
        WaveControlService.lastGestureFlow.collectLatest { event ->
            lastEvent = event
        }
    }

    // Manage Service Start/Stop based on Master switch
    LaunchedEffect(settings.isEnabled) {
        if (settings.isEnabled) {
            if (!hasCameraPermission) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            WaveControlService.startService(context)
        } else {
            WaveControlService.stopService(context)
        }
    }

    val actionExecutor = remember { WaveActionExecutor(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wave Control",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.TouchApp, contentDescription = "Aksi") },
                    label = { Text("Aksi Gerakan") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Tune, contentDescription = "Sensitivitas") },
                    label = { Text("Sensitivitas") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Help, contentDescription = "Panduan") },
                    label = { Text("Panduan") }
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = selectedTab,
            label = "tabFade",
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                0 -> HomeScreen(
                    settings = settings,
                    isServiceRunning = WaveControlService.isRunning,
                    lastDetectedEvent = lastEvent,
                    hasCameraPermission = hasCameraPermission,
                    hasNotificationPermission = hasNotificationPermission,
                    isAccessibilityEnabled = isAccessibilityEnabled,
                    canDrawOverlays = canDrawOverlays,
                    onToggleMasterSwitch = { enabled ->
                        repository.updateSettings { it.copy(isEnabled = enabled) }
                    },
                    onRequestCamera = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    onRequestNotification = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                    onRequestAccessibility = {
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                        Toast.makeText(context, "Cari dan aktifkan 'Wave Control' di daftar layanan", Toast.LENGTH_LONG).show()
                    },
                    onRequestOverlay = {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context.packageName}")
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    }
                )

                1 -> GestureConfigScreen(
                    settings = settings,
                    isAccessibilityEnabled = isAccessibilityEnabled,
                    onUpdateGestureAction = { gesture, action ->
                        repository.updateSettings { s ->
                            when (gesture) {
                                GestureType.WAVE_LEFT -> s.copy(actionWaveLeft = action)
                                GestureType.WAVE_RIGHT -> s.copy(actionWaveRight = action)
                                GestureType.WAVE_UP -> s.copy(actionWaveUp = action)
                                GestureType.WAVE_DOWN -> s.copy(actionWaveDown = action)
                                GestureType.HAND_HOVER -> s.copy(actionHandHover = action)
                                GestureType.PROXIMITY_WAVE -> s.copy(actionProximity = action)
                                GestureType.NONE -> s
                            }
                        }
                    },
                    onTestAction = { action ->
                        val executed = actionExecutor.execute(action, settings)
                        Toast.makeText(
                            context,
                            if (executed) "Menjalankan: ${action.title}" else "Aksi butuh izin atau tidak didukung",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                2 -> SettingsScreen(
                    settings = settings,
                    onUpdateSettings = { update ->
                        repository.updateSettings(update)
                    }
                )

                3 -> GuideScreen()
            }
        }
    }
}
