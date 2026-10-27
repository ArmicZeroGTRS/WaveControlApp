package com.wavecontrol.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatterySaver
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wavecontrol.app.R
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.theme.*

@Composable
fun SettingsScreen(
    settings: WaveSettings,
    onUpdateSettings: ((WaveSettings) -> WaveSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hardware Profiles Mode Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLow),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CyberTeal.copy(alpha = 0.35f), HairlineBorder.copy(alpha = 0.2f))))
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
                            .background(CyberTealDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Hardware Profiles",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LuminousCyan
                        )
                        Text(
                            text = "Calibrated • Latency < 8ms",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = CyberTeal
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(KineticSurfaceHighest)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "v1.3",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Section 1: Appearance & Dynamics
        SectionHeader(icon = Icons.Default.Palette, title = "APPEARANCE & DYNAMICS")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLow),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HairlineBorder.copy(alpha = 0.3f), Color.Transparent)))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Theme Segmented Control
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Interface Theme",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Pure obsidian tone tailored for OLED power thrift.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(KineticSurfaceHighest)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ThemePillButton(
                            title = "Dark",
                            icon = Icons.Default.DarkMode,
                            isSelected = settings.themeMode == "dark",
                            onClick = { onUpdateSettings { it.copy(themeMode = "dark") } },
                            modifier = Modifier.weight(1f)
                        )
                        ThemePillButton(
                            title = "Light",
                            icon = Icons.Default.LightMode,
                            isSelected = settings.themeMode == "light",
                            onClick = { onUpdateSettings { it.copy(themeMode = "light") } },
                            modifier = Modifier.weight(1f)
                        )
                        ThemePillButton(
                            title = "Auto",
                            icon = Icons.Default.PhonelinkSetup,
                            isSelected = settings.themeMode == "system",
                            onClick = { onUpdateSettings { it.copy(themeMode = "system") } },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Spatial Haptic Pulse Slider
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
                                imageVector = Icons.Default.Vibration,
                                contentDescription = null,
                                tint = CyberTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Spatial Haptic Pulse",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "${(settings.hapticIntensity * 100).toInt()}%",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LuminousCyan
                        )
                    }

                    Text(
                        text = "Actuator resonance on recognized mid-air vector crossings.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Slider(
                        value = settings.hapticIntensity,
                        onValueChange = { onUpdateSettings { s -> s.copy(hapticIntensity = it) } },
                        valueRange = 0.0f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = LuminousCyan,
                            inactiveTrackColor = KineticSurfaceHighest
                        )
                    )
                }

                // Reduced Kinematics Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Reduced Spatial Kinematics",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Suppress radar sweep particle drift to prioritize raw FPS.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = settings.reducedMotion,
                        onCheckedChange = { onUpdateSettings { s -> s.copy(reducedMotion = it) } },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = CyberTeal
                        )
                    )
                }
            }
        }

        // Section 2: Engine & Sensor Telemetry
        SectionHeader(icon = Icons.Default.Memory, title = "ENGINE & SENSOR TELEMETRY")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLow),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HairlineBorder.copy(alpha = 0.3f), Color.Transparent)))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Tracking Pipeline Card (Active)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(KineticSurfaceHigh)
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
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(KineticSurfaceHighest),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Videocam, null, tint = LuminousCyan, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Optical Flow (Neural)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LuminousCyan
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(LuminousCyan)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("ACTIVE", fontFamily = FontFamily.Monospace, fontSize = 8.sp, color = Color(0xFF00363A), fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = "High-dimensional motion diff & centroid tracking",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(Icons.Default.CheckCircle, null, tint = LuminousCyan, modifier = Modifier.size(18.dp))
                }

                // Sensitivity Slider
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Speed, null, tint = CyberTeal, modifier = Modifier.size(16.dp))
                            Text("Gesture Sensitivity", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Text("${(settings.sensitivity * 100).toInt()}%", fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = LuminousCyan)
                    }
                    Text("Controls the motion threshold required to trigger waves.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Slider(
                        value = settings.sensitivity,
                        onValueChange = { onUpdateSettings { s -> s.copy(sensitivity = it) } },
                        valueRange = 0.1f..0.95f,
                        colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = LuminousCyan, inactiveTrackColor = KineticSurfaceHighest)
                    )
                }

                // Swap Axes Switch (crucial fix for inverted cameras)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.SwapVert, null, tint = CyberTeal, modifier = Modifier.size(16.dp))
                            Text("Swap Horizontal & Vertical Axes", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Text("Enable if hand gestures detect vertical motion instead of horizontal.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Switch(
                        checked = settings.swapAxes,
                        onCheckedChange = { onUpdateSettings { s -> s.copy(swapAxes = it) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = CyberTeal)
                    )
                }

                // Pause on Screen Off Switch (Battery & Sensor Saver)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.BatterySaver, null, tint = OperationalEmerald, modifier = Modifier.size(16.dp))
                            Text("Pause When Screen Off", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Text("Unbinds camera and pauses sensors when device is locked.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Switch(
                        checked = settings.pauseOnScreenOff,
                        onCheckedChange = { onUpdateSettings { s -> s.copy(pauseOnScreenOff = it) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = OperationalEmerald)
                    )
                }
            }
        }

        // Section 3: Sandboxed Security Vault
        SectionHeader(icon = Icons.Default.Security, title = "SANDBOXED SECURITY VAULT")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLow),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(HairlineBorder.copy(alpha = 0.3f), Color.Transparent)))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = OperationalEmerald, modifier = Modifier.size(18.dp))
                    Text(
                        text = "100% Mobile Banking Compatible",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "• Zero Accessibility Services used\n• Zero Restricted Settings required\n• Camera frames analyzed in memory only (never saved)\n• 100% sandboxed Android 15 architecture",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }

        // Section 4: Brand Identity & About Engine
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceLowest),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(CyberTeal.copy(alpha = 0.4f), HairlineBorder.copy(alpha = 0.2f))))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_wavecontrol_wordmark),
                    contentDescription = "WaveControl Wordmark",
                    modifier = Modifier.height(34.dp)
                )

                Text(
                    text = "Touchless Gesture Control • Kinetic Telemetry Spatial UI",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = CyberTeal
                )

                Text(
                    text = "Version 1.3 (Build 4) • Android 15 Ready",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = CyberTeal, modifier = Modifier.size(15.dp))
        Text(
            text = title,
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun ThemePillButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(if (isSelected) LuminousCyan else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF00363A) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF00363A) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
