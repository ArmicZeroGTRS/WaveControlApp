package com.wavecontrol.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wavecontrol.app.detector.DetectionEvent
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.ui.components.CameraPreviewCard
import com.wavecontrol.app.ui.components.PermissionCard

@Composable
fun HomeScreen(
    settings: WaveSettings,
    isServiceRunning: Boolean,
    lastDetectedEvent: DetectionEvent?,
    hasCameraPermission: Boolean,
    hasNotificationPermission: Boolean,
    onToggleMasterSwitch: (Boolean) -> Unit,
    onToggleSwapAxes: () -> Unit,
    onRequestCamera: () -> Unit,
    onRequestNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val cardColor by animateColorAsState(
        targetValue = if (settings.isEnabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(400),
        label = "cardColor"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Master Control Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    if (settings.isEnabled) Color(0xFF10B981) else Color(0xFF64748B)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PowerSettingsNew,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = if (settings.isEnabled) "Wave Control AKTIF" else "Wave Control NONAKTIF",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (settings.isEnabled) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (settings.isEnabled) "Mendeteksi gerakan tangan di latar belakang" else "Sentuh sakelar untuk mengaktifkan",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (settings.isEnabled) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Switch(
                        checked = settings.isEnabled,
                        onCheckedChange = { onToggleMasterSwitch(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF10B981)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sensors Status Badges Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SensorStatusChip(
                        name = "Kamera Depan",
                        isActive = settings.isEnabled && settings.isCameraWaveEnabled && hasCameraPermission,
                        modifier = Modifier.weight(1f)
                    )
                    SensorStatusChip(
                        name = "Proximity",
                        isActive = settings.isEnabled && settings.isProximityWaveEnabled,
                        modifier = Modifier.weight(1f)
                    )
                    SensorStatusChip(
                        name = "M-Banking Safe",
                        isActive = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Real-time Gesture Event Banner (Live feedback)
        AnimatedVisibility(visible = lastDetectedEvent != null && lastDetectedEvent.gesture != GestureType.NONE) {
            val event = lastDetectedEvent ?: return@AnimatedVisibility
            val action = settings.getActionForGesture(event.gesture)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF38BDF8).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = event.gesture.emoji, fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Gerakan Terdeteksi: ${event.gesture.title}",
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Menjalankan: ${action.title}",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Camera Preview & Live Motion Radar Card
        CameraPreviewCard(
            hasCameraPermission = hasCameraPermission,
            isServiceRunning = isServiceRunning,
            lastDetectedGesture = lastDetectedEvent?.gesture ?: GestureType.NONE,
            swapAxes = settings.swapAxes,
            onToggleSwapAxes = onToggleSwapAxes
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Permissions Card (Safe, No Accessibility, No Restricted Settings)
        PermissionCard(
            hasCameraPermission = hasCameraPermission,
            hasNotificationPermission = hasNotificationPermission,
            onRequestCamera = onRequestCamera,
            onRequestNotification = onRequestNotification
        )
    }
}

@Composable
private fun SensorStatusChip(
    name: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isActive) Color(0xFF10B981).copy(alpha = 0.2f)
                else Color(0xFF64748B).copy(alpha = 0.15f)
            )
            .padding(vertical = 6.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Color(0xFF10B981) else Color(0xFF94A3B8))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) Color(0xFF047857) else Color(0xFF64748B)
            )
        }
    }
}
