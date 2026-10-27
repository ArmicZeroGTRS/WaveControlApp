package com.wavecontrol.app.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveAction
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestureConfigScreen(
    settings: WaveSettings,
    onUpdateGestureAction: (GestureType, WaveAction) -> Unit,
    onToggleGestureEnabled: (GestureType, Boolean) -> Unit,
    onTestAction: (WaveAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedDomain by remember { mutableStateOf("All Domains") }
    var editingGesture by remember { mutableStateOf<GestureType?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val domains = listOf("All Domains", "Media Control", "System Audio", "Scrolling", "Navigation")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Telemetry HUD Overview Strip
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = KineticSurfaceHigh),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CyberTeal.copy(alpha = 0.3f), HairlineBorder.copy(alpha = 0.2f))))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CyberTeal)
                        )
                        Text(
                            text = "Kinetic Radar Engine",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LuminousCyan
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(KineticSurfaceHighest)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (settings.isEnabled) "ONLINE" else "STANDBY",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (settings.isEnabled) CyberTeal else Color.Gray
                        )
                    }
                }

                // 3 Stats Grid
                val activeNodesCount = listOf(
                    settings.isWaveLeftEnabled,
                    settings.isWaveRightEnabled,
                    settings.isWaveUpEnabled,
                    settings.isWaveDownEnabled,
                    settings.isHandHoverEnabled,
                    settings.isProximityActionEnabled
                ).count { it }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Stat 1: Active Mesh
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(KineticSurfaceLow)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "ACTIVE MESH",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$activeNodesCount",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LuminousCyan
                                )
                                Text(
                                    text = "/ 6",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "Bound Vectors",
                                fontSize = 10.sp,
                                color = CyberTeal
                            )
                        }
                    }

                    // Stat 2: Interference
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(KineticSurfaceLow)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "INTERFERENCE",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = "0",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberTeal
                                )
                                Text(
                                    text = "err",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "Zero Conflict",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Stat 3: Profile
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(KineticSurfaceLow)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "PROFILE",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Dynamic",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = LuminousCyan,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )
                            Text(
                                text = "60Hz Optical",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Domain Filter Pills
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "QUICK ACTION FILTER",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(domains) { domain ->
                    val isSelected = domain == selectedDomain
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) LuminousCyan else KineticSurfaceHigh)
                            .clickable { selectedDomain = domain }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = domain,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF00363A) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Gestures List Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CONFIGURED GESTURE NODES",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Text(
                text = "OPT-VEC 2.4",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = CyberTeal
            )
        }

        // 4 Configured Gesture Node Cards + 2 Auxiliary Nodes (with Independent On/Off Toggles)
        GestureNodeCard(
            code = "W-L",
            title = "Wave Left",
            domain = "Media Control",
            icon = Icons.Default.SkipPrevious,
            currentAction = settings.actionWaveLeft,
            isEnabled = settings.isWaveLeftEnabled,
            onToggleEnabled = { onToggleGestureEnabled(GestureType.WAVE_LEFT, it) },
            onRemapClick = { editingGesture = GestureType.WAVE_LEFT },
            onTest = { onTestAction(settings.actionWaveLeft) }
        )

        GestureNodeCard(
            code = "W-R",
            title = "Wave Right",
            domain = "Media Control",
            icon = Icons.Default.SkipNext,
            currentAction = settings.actionWaveRight,
            isEnabled = settings.isWaveRightEnabled,
            onToggleEnabled = { onToggleGestureEnabled(GestureType.WAVE_RIGHT, it) },
            onRemapClick = { editingGesture = GestureType.WAVE_RIGHT },
            onTest = { onTestAction(settings.actionWaveRight) }
        )

        GestureNodeCard(
            code = "W-U",
            title = "Wave Up",
            domain = "System Audio",
            icon = Icons.Default.VolumeUp,
            currentAction = settings.actionWaveUp,
            isEnabled = settings.isWaveUpEnabled,
            onToggleEnabled = { onToggleGestureEnabled(GestureType.WAVE_UP, it) },
            onRemapClick = { editingGesture = GestureType.WAVE_UP },
            onTest = { onTestAction(settings.actionWaveUp) }
        )

        GestureNodeCard(
            code = "W-D",
            title = "Wave Down",
            domain = "System Audio",
            icon = Icons.Default.VolumeDown,
            currentAction = settings.actionWaveDown,
            isEnabled = settings.isWaveDownEnabled,
            onToggleEnabled = { onToggleGestureEnabled(GestureType.WAVE_DOWN, it) },
            onRemapClick = { editingGesture = GestureType.WAVE_DOWN },
            onTest = { onTestAction(settings.actionWaveDown) }
        )

        GestureNodeCard(
            code = "H-H",
            title = "Hand Hover / Hold",
            domain = "Media Control",
            icon = Icons.Default.FrontHand,
            currentAction = settings.actionHandHover,
            isEnabled = settings.isHandHoverEnabled,
            onToggleEnabled = { onToggleGestureEnabled(GestureType.HAND_HOVER, it) },
            onRemapClick = { editingGesture = GestureType.HAND_HOVER },
            onTest = { onTestAction(settings.actionHandHover) }
        )

        GestureNodeCard(
            code = "P-W",
            title = "Proximity Wave",
            domain = "Optical Sensor",
            icon = Icons.Default.Sensors,
            currentAction = settings.actionProximity,
            isEnabled = settings.isProximityActionEnabled,
            onToggleEnabled = { onToggleGestureEnabled(GestureType.PROXIMITY_WAVE, it) },
            onRemapClick = { editingGesture = GestureType.PROXIMITY_WAVE },
            onTest = { onTestAction(settings.actionProximity) }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    // Action Remapping Bottom Sheet
    editingGesture?.let { gesture ->
        ModalBottomSheet(
            onDismissRequest = { editingGesture = null },
            sheetState = sheetState,
            containerColor = KineticSurfaceHigh
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "REMAP ACTION",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = CyberTeal
                        )
                        Text(
                            text = "Assign to ${gesture.title}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = LuminousCyan
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(KineticSurfaceHighest),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = gesture.emoji, fontSize = 18.sp)
                    }
                }

                val currentAction = settings.getActionForGesture(gesture)
                val availableActions = WaveAction.values().filter { it != WaveAction.NONE }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableActions.forEach { action ->
                        val isSelected = action == currentAction
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) KineticSurfaceLowest else KineticSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) LuminousCyan else HairlineBorder.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onUpdateGestureAction(gesture, action)
                                    editingGesture = null
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = action.title,
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) LuminousCyan else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = action.description,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = LuminousCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun GestureNodeCard(
    code: String,
    title: String,
    domain: String,
    icon: ImageVector,
    currentAction: WaveAction,
    isEnabled: Boolean,
    onToggleEnabled: (Boolean) -> Unit,
    onRemapClick: () -> Unit,
    onTest: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isEnabled) 1.0f else 0.58f),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = KineticSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(
                    if (isEnabled) CyberTeal.copy(alpha = 0.35f) else HairlineBorder.copy(alpha = 0.2f),
                    Color.Transparent
                )
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Icon + Title + Switch Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(KineticSurfaceHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isEnabled) LuminousCyan else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .clip(RoundedCornerShape(topEnd = 4.dp))
                                .background(KineticSurfaceLowest)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = code,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.sp,
                                color = if (isEnabled) LuminousCyan else Color.Gray
                            )
                        }
                    }

                    Column {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Domain: $domain",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = if (isEnabled) CyberTeal else Color.Gray
                        )
                    }
                }

                // Interactive Toggle Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isEnabled) "ACTIVE" else "OFF",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEnabled) OperationalEmerald else Color.Gray
                    )
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = onToggleEnabled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = CyberTeal,
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = KineticSurfaceHighest
                        )
                    )
                }
            }

            // Bound Action Selector Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(KineticSurfaceLow)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BOUND ACTION",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (isEnabled) currentAction.title else "${currentAction.title} (Disabled)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isEnabled) MaterialTheme.colorScheme.onSurface else Color.Gray
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Test button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(KineticSurfaceHigh)
                            .clickable(enabled = isEnabled) { onTest() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "TEST",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isEnabled) CyberTeal else Color.Gray
                        )
                    }

                    // Remap button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(KineticSurfaceHighest)
                            .clickable { onRemapClick() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "REMAP",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = LuminousCyan
                            )
                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = LuminousCyan,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
