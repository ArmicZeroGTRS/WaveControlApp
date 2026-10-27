package com.wavecontrol.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material.icons.filled.SwipeRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.ViewSidebar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FloatingHudScreen(
    settings: WaveSettings,
    onUpdateSettings: ((WaveSettings) -> WaveSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var isSimulatingGesture by remember { mutableStateOf(false) }
    var simulationText by remember { mutableStateOf("READY") }

    val canDrawOverlays = remember {
        try {
            Settings.canDrawOverlays(context)
        } catch (_: Exception) {
            false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Studio Viewport Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Stream,
                    contentDescription = null,
                    tint = CyberTeal,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Floating HUD Studio",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = LuminousCyan
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(KineticSurfaceHigh)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CyberTeal)
                    )
                    Text(
                        text = "ACTIVE STREAM",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTeal
                    )
                }
            }
        }

        // Simulated Phone Display Viewport (16:9 Aspect Ratio)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLowest),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(CyberTeal.copy(alpha = 0.35f), HairlineBorder.copy(alpha = 0.2f))))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // Header Diagnostics inside Simulator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(KineticSurfaceLow.copy(alpha = 0.9f))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterCenterFocus,
                            contentDescription = null,
                            tint = CyberTeal,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "PREVIEW // HUD SIMULATOR",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = CyberTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = simulationText,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Center Island Target / Live HUD Capsule
                Column(
                    modifier = Modifier
                        .align(
                            when (settings.hudAnchorPosition) {
                                "TOP_RIGHT" -> Alignment.TopEnd
                                "DYNAMIC_EDGE" -> Alignment.CenterEnd
                                else -> Alignment.TopCenter
                            }
                        )
                        .padding(top = if (settings.hudAnchorPosition == "CENTER_ISLAND") 36.dp else 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isSimulatingGesture) {
                        // Floating Capsule
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(KineticSurfaceHigh.copy(alpha = 0.95f))
                                .border(1.dp, LuminousCyan.copy(alpha = 0.6f), CircleShape)
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(CyberTeal.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwipeRight,
                                    contentDescription = null,
                                    tint = CyberTeal,
                                    modifier = Modifier.size(15.dp)
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "WAVE RIGHT",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LuminousCyan
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(CyberTeal)
                                    )
                                    Text(
                                        text = "NEXT TRACK",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CyberTeal
                                    )
                                }
                                Text(
                                    text = "LATENCY: 14ms • DISPATCHED",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 8.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(CyberTeal)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(KineticSurfaceLow.copy(alpha = 0.5f))
                                .border(1.dp, HairlineBorder.copy(alpha = 0.4f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "HUD Anchor: ${settings.hudAnchorPosition}",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Viewport Footer Trigger Area
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (canDrawOverlays) "MODE: SYSTEM OVERLAY" else "MODE: IN-APP TOAST FALLBACK",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = {
                            scope.launch {
                                isSimulatingGesture = true
                                simulationText = "TRIGGERING..."
                                delay(1200)
                                isSimulatingGesture = false
                                simulationText = "READY"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberTeal,
                            contentColor = Color(0xFF003732)
                        ),
                        shape = CircleShape,
                        modifier = Modifier.height(32.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Bolt, null, modifier = Modifier.size(13.dp))
                            Text(
                                text = "TRIGGER GESTURE",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Overlay Privilege Status Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLow),
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(KineticSurfaceHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = if (canDrawOverlays) OperationalEmerald else CyberTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Overlay Privilege",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (canDrawOverlays) OperationalEmerald.copy(alpha = 0.15f) else KineticSurfaceHighest)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (canDrawOverlays) "GRANTED" else "OPTIONAL",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (canDrawOverlays) OperationalEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = if (canDrawOverlays) "Global System HUD enabled on Android" else "Touch to configure display over other apps",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (!canDrawOverlays) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(KineticSurfaceHighest)
                            .clickable {
                                try {
                                    val intent = Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:${context.packageName}")
                                    )
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "ENABLE",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = LuminousCyan
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(OperationalEmerald),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // HUD Visual Topology Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                tint = CyberTeal,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "HUD Visual Topology",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = LuminousCyan
            )
        }

        // Screen Anchor Target Configuration Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLow),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HairlineBorder.copy(alpha = 0.3f), Color.Transparent)))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Screen Anchor Target",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = settings.hudAnchorPosition.replace("_", " "),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberTeal
                    )
                }

                // 3 Anchor buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnchorOptionButton(
                        title = "Top-Right",
                        icon = Icons.Default.NorthEast,
                        isSelected = settings.hudAnchorPosition == "TOP_RIGHT",
                        onClick = { onUpdateSettings { it.copy(hudAnchorPosition = "TOP_RIGHT") } },
                        modifier = Modifier.weight(1f)
                    )
                    AnchorOptionButton(
                        title = "Center Island",
                        icon = Icons.Default.ExpandLess,
                        isSelected = settings.hudAnchorPosition == "CENTER_ISLAND",
                        onClick = { onUpdateSettings { it.copy(hudAnchorPosition = "CENTER_ISLAND") } },
                        modifier = Modifier.weight(1f)
                    )
                    AnchorOptionButton(
                        title = "Dynamic Edge",
                        icon = Icons.Default.ViewSidebar,
                        isSelected = settings.hudAnchorPosition == "DYNAMIC_EDGE",
                        onClick = { onUpdateSettings { it.copy(hudAnchorPosition = "DYNAMIC_EDGE") } },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // HUD Toggle Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLow),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HairlineBorder.copy(alpha = 0.3f), Color.Transparent)))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Global HUD Notifications",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Display glowing telemetry capsule upon mid-air gesture trigger",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = settings.isFloatingHudEnabled,
                    onCheckedChange = { enabled ->
                        onUpdateSettings { it.copy(isFloatingHudEnabled = enabled) }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = CyberTeal
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AnchorOptionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) CyberTeal else KineticSurface)
            .border(
                1.dp,
                if (isSelected) LuminousCyan else HairlineBorder.copy(alpha = 0.4f),
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF003732) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF003732) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
