package com.wavecontrol.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wavecontrol.app.R
import com.wavecontrol.app.detector.DetectionEvent
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.theme.*
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
    onRequestCamera: () -> Unit,
    onRequestNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Pulse animation for radar rings
    val infiniteTransition = rememberInfiniteTransition(label = "radarPulse")
    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse1"
    )
    val pulseScale2 by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse2"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Telemetry Status Ribbon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Engine Active Badge
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(KineticSurfaceHigh)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (settings.isEnabled) OperationalEmerald else Color.Gray)
                    )
                    Text(
                        text = if (settings.isEnabled) "ENGINE ACTIVE" else "STANDBY",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (settings.isEnabled) CyberTeal else Color.Gray
                    )
                }

                // 60 FPS Camera Pill
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(KineticSurfaceHigh.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = null,
                        tint = CyberTeal,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "60 FPS",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // M-Banking Safe SYNC Pill
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(KineticSurfaceHigh.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = CyberTeal,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "BANK SAFE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Wordmark Mini Logo
            Image(
                painter = painterResource(id = R.drawable.ic_wavecontrol_wordmark),
                contentDescription = "WaveControl Brand",
                modifier = Modifier.height(22.dp)
            )
        }

        // Ephemeral Mini Toast / Event Banner
        AnimatedVisibility(visible = lastDetectedEvent != null) {
            lastDetectedEvent?.let { event ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = KineticSurfaceHigh),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(LuminousCyan.copy(alpha = 0.4f), Color.Transparent)))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(CyberTeal.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = event.gesture.emoji,
                                    fontSize = 16.sp
                                )
                            }
                            Column {
                                Text(
                                    text = "SPATIAL PIPELINE TRIGGERED",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTeal
                                )
                                Text(
                                    text = "${event.gesture.title} • Executed",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(KineticSurfaceHighest)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "JUST NOW",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Permissions Card if missing
        if (!hasCameraPermission || !hasNotificationPermission) {
            PermissionCard(
                hasCameraPermission = hasCameraPermission,
                hasNotificationPermission = hasNotificationPermission,
                onRequestCamera = onRequestCamera,
                onRequestNotification = onRequestNotification
            )
        }

        // Central Interactive Wave Visualizer Hero (Radar Viewport)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.25f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLowest),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(LuminousCyan.copy(alpha = 0.35f), HairlineBorder.copy(alpha = 0.2f))))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top Telemetry Row inside Viewport
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(KineticSurfaceHigh.copy(alpha = 0.9f))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (hasCameraPermission && settings.isEnabled) OperationalEmerald else TerminalRed)
                        )
                        Text(
                            text = if (hasCameraPermission && settings.isEnabled) "CAMERA LOCK" else "SENSOR IDLE",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasCameraPermission && settings.isEnabled) CyberTeal else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(KineticSurfaceHigh.copy(alpha = 0.8f))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = LuminousCyan,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "14ms LATENCY",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = LuminousCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Concentric Interactive Radar Wave Graphic
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (settings.isEnabled) {
                        // Outer pulsing wave ring
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .scale(pulseScale2)
                                .clip(CircleShape)
                                .border(1.dp, LuminousCyan.copy(alpha = 0.15f), CircleShape)
                                .background(LuminousCyan.copy(alpha = 0.03f))
                        )
                        // Middle wave ring
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .scale(pulseScale1)
                                .clip(CircleShape)
                                .border(1.dp, CyberTeal.copy(alpha = 0.25f), CircleShape)
                                .background(CyberTeal.copy(alpha = 0.05f))
                        )
                    }

                    // Inner central emblem hub
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(KineticSurfaceHigh)
                            .border(1.5.dp, if (settings.isEnabled) LuminousCyan else HairlineBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_wavecontrol_symbol),
                            contentDescription = "WaveControl Symbol",
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    // Directional Target Quadrants
                    // UP
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .clip(RoundedCornerShape(4.dp))
                            .background(KineticSurfaceHighest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Icon(Icons.Default.ArrowUpward, null, tint = CyberTeal, modifier = Modifier.size(10.dp))
                            Text("UP", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = CyberTeal)
                        }
                    }

                    // DOWN
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .clip(RoundedCornerShape(4.dp))
                            .background(KineticSurfaceHighest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Icon(Icons.Default.ArrowDownward, null, tint = CyberTeal, modifier = Modifier.size(10.dp))
                            Text("DOWN", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = CyberTeal)
                        }
                    }

                    // PREV
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clip(RoundedCornerShape(4.dp))
                            .background(KineticSurfaceHighest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("PREV", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = CyberTeal)
                    }

                    // NEXT
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(4.dp))
                            .background(KineticSurfaceHighest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("NEXT", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = LuminousCyan, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ArrowForward, null, tint = LuminousCyan, modifier = Modifier.size(10.dp))
                        }
                    }
                }

                // Bottom Proximity Range Status Bar inside Viewport
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(KineticSurfaceLow.copy(alpha = 0.95f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sensors,
                            contentDescription = null,
                            tint = CyberTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Optical Range: 15–35cm",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = if (settings.isEnabled) "98% CONF" else "IDLE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (settings.isEnabled) CyberTeal else Color.Gray
                    )
                }
            }
        }

        // Primary Engine Start / Pause Command Pill
        Button(
            onClick = { onToggleMasterSwitch(!settings.isEnabled) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (settings.isEnabled) LuminousCyan else KineticSurfaceHigh,
                contentColor = if (settings.isEnabled) Color(0xFF00363A) else Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = if (settings.isEnabled) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (settings.isEnabled) "PAUSE ENGINE" else "ACTIVATE ENGINE",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Real-Time Optical Camera Preview Debug Card
        CameraPreviewCard(
            hasCameraPermission = hasCameraPermission,
            isServiceRunning = settings.isEnabled && hasCameraPermission,
            lastDetectedGesture = lastDetectedEvent?.gesture ?: GestureType.NONE
        )

        // Live Telemetry & Spatial Diagnostics Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active Telemetry",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "REAL-TIME BUS",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = CyberTeal,
                letterSpacing = 1.sp
            )
        }

        // Last Gesture Broadcast Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HairlineBorder.copy(alpha = 0.3f), Color.Transparent)))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(KineticSurfaceHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = LuminousCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "LAST RECOGNIZED GESTURE",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${lastDetectedEvent?.gesture?.title ?: "Wave Right"} → Next Track",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LuminousCyan
                        )
                        Text(
                            text = "Auto-dispatched to Foreground Audio",
                            fontSize = 11.sp,
                            color = CyberTeal
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(CyberTeal)
                )
            }
        }

        // Sensor Fusion Array Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HairlineBorder.copy(alpha = 0.3f), Color.Transparent)))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(KineticSurfaceHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = CyberTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "SENSOR FUSION ARRAY",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Front RGB Camera + IR Proximity",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = "60 FPS Native",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = LuminousCyan
                            )
                            Text("•", fontSize = 9.sp, color = Color.Gray)
                            Text(
                                text = "Zero Overhead",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = CyberTeal
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(OperationalEmerald)
                )
            }
        }

        // Bank Safety & Security Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HairlineBorder.copy(alpha = 0.3f), Color.Transparent)))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(KineticSurfaceHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = OperationalEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "BANKING APP SAFE (ANDROID 15+)",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Direct System Key & Audio Hooks",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Zero Accessibility Services • 100% Mobile Banking Compatible",
                            fontSize = 10.sp,
                            color = OperationalEmerald
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CyberTeal.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "SECURE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTeal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
