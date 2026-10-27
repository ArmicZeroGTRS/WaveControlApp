package com.wavecontrol.app.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.wavecontrol.app.R
import com.wavecontrol.app.detector.DetectionEvent
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveAction
import com.wavecontrol.app.model.WaveSettingsRepository
import com.wavecontrol.app.service.WaveActionExecutor
import com.wavecontrol.app.service.WaveControlService
import com.wavecontrol.app.theme.CyberTeal
import com.wavecontrol.app.theme.KineticBackground
import com.wavecontrol.app.theme.KineticSurface
import com.wavecontrol.app.theme.KineticSurfaceHigh
import com.wavecontrol.app.theme.LuminousCyan
import com.wavecontrol.app.theme.OperationalEmerald
import com.wavecontrol.app.theme.WaveControlTheme
import com.wavecontrol.app.ui.screens.FloatingHudScreen
import com.wavecontrol.app.ui.screens.GestureConfigScreen
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

    // Permission Launchers
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (granted) {
            WaveControlService.notifyCameraPermissionGranted(context)
        } else {
            Toast.makeText(context, "Camera permission is required to detect gestures!", Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotificationPermission = granted
    }

    // Recheck permissions on resume
    DisposableEffect(Unit) {
        val checkPermissions = {
            val cameraOk = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            if (cameraOk != hasCameraPermission) {
                hasCameraPermission = cameraOk
                if (cameraOk) {
                    WaveControlService.notifyCameraPermissionGranted(context)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                hasNotificationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            }
        }
        checkPermissions()
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

    WaveControlTheme(themeMode = settings.themeMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_wavecontrol_symbol),
                                contentDescription = "Logo",
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    text = "WAVECONTROL",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = LuminousCyan
                                )
                                Text(
                                    text = if (settings.isEnabled) "Kinetic Telemetry • Active" else "Kinetic Telemetry • Standby",
                                    fontSize = 10.sp,
                                    color = if (settings.isEnabled) CyberTeal else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { selectedTab = 3 }) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Radar, contentDescription = "Radar") },
                        label = { Text("Radar", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LuminousCyan,
                            selectedTextColor = LuminousCyan,
                            indicatorColor = KineticSurfaceHigh
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.TouchApp, contentDescription = "Gestures") },
                        label = { Text("Gestures", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LuminousCyan,
                            selectedTextColor = LuminousCyan,
                            indicatorColor = KineticSurfaceHigh
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Stream, contentDescription = "HUD") },
                        label = { Text("HUD", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LuminousCyan,
                            selectedTextColor = LuminousCyan,
                            indicatorColor = KineticSurfaceHigh
                        )
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.Tune, contentDescription = "Settings") },
                        label = { Text("Settings", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LuminousCyan,
                            selectedTextColor = LuminousCyan,
                            indicatorColor = KineticSurfaceHigh
                        )
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
                        onToggleMasterSwitch = { enabled ->
                            repository.updateSettings { it.copy(isEnabled = enabled) }
                        },
                        onRequestCamera = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                        onRequestNotification = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                    )

                    1 -> GestureConfigScreen(
                        settings = settings,
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
                        onToggleGestureEnabled = { gesture, enabled ->
                            repository.updateSettings { s ->
                                when (gesture) {
                                    GestureType.WAVE_LEFT -> s.copy(isWaveLeftEnabled = enabled)
                                    GestureType.WAVE_RIGHT -> s.copy(isWaveRightEnabled = enabled)
                                    GestureType.WAVE_UP -> s.copy(isWaveUpEnabled = enabled)
                                    GestureType.WAVE_DOWN -> s.copy(isWaveDownEnabled = enabled)
                                    GestureType.HAND_HOVER -> s.copy(isHandHoverEnabled = enabled)
                                    GestureType.PROXIMITY_WAVE -> s.copy(isProximityActionEnabled = enabled)
                                    GestureType.NONE -> s
                                }
                            }
                        },
                        onTestAction = { action ->
                            actionExecutor.execute(action, settings)
                        }
                    )

                    2 -> FloatingHudScreen(
                        settings = settings,
                        onUpdateSettings = { updater -> repository.updateSettings(updater) }
                    )

                    3 -> SettingsScreen(
                        settings = settings,
                        onUpdateSettings = { updater -> repository.updateSettings(updater) }
                    )
                }
            }
        }
    }
}
