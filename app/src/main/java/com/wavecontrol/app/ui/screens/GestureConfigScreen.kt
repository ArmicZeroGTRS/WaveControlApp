package com.wavecontrol.app.ui.screens

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
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveAction
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.ui.components.GestureCard

@Composable
fun GestureConfigScreen(
    settings: WaveSettings,
    onUpdateGestureAction: (GestureType, WaveAction) -> Unit,
    onTestAction: (WaveAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Gesture Action Mapping",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Configure actions for each hand wave direction. All actions are 100% safe and do not require accessibility services or restricted settings.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // SECTION 1: HORIZONTAL GESTURES
        GestureSectionHeader(
            icon = Icons.Default.SwapHoriz,
            title = "Horizontal Gestures (Left & Right)",
            subtitle = "Horizontal hand wave from side to side"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GestureCard(
                gesture = GestureType.WAVE_LEFT,
                selectedAction = settings.actionWaveLeft,
                onActionSelected = { onUpdateGestureAction(GestureType.WAVE_LEFT, it) },
                onTestAction = onTestAction
            )

            GestureCard(
                gesture = GestureType.WAVE_RIGHT,
                selectedAction = settings.actionWaveRight,
                onActionSelected = { onUpdateGestureAction(GestureType.WAVE_RIGHT, it) },
                onTestAction = onTestAction
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 2: VERTICAL GESTURES
        GestureSectionHeader(
            icon = Icons.Default.SwapVert,
            title = "Vertical Gestures (Up & Down)",
            subtitle = "Vertical hand wave moving up or down"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GestureCard(
                gesture = GestureType.WAVE_UP,
                selectedAction = settings.actionWaveUp,
                onActionSelected = { onUpdateGestureAction(GestureType.WAVE_UP, it) },
                onTestAction = onTestAction
            )

            GestureCard(
                gesture = GestureType.WAVE_DOWN,
                selectedAction = settings.actionWaveDown,
                onActionSelected = { onUpdateGestureAction(GestureType.WAVE_DOWN, it) },
                onTestAction = onTestAction
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 3: STATIC & PROXIMITY GESTURES
        GestureSectionHeader(
            icon = Icons.Default.TouchApp,
            title = "Static & Proximity Gestures",
            subtitle = "Holding hand still in front of camera or waving near sensor"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GestureCard(
                gesture = GestureType.HAND_HOVER,
                selectedAction = settings.actionHandHover,
                onActionSelected = { onUpdateGestureAction(GestureType.HAND_HOVER, it) },
                onTestAction = onTestAction
            )

            GestureCard(
                gesture = GestureType.PROXIMITY_WAVE,
                selectedAction = settings.actionProximity,
                onActionSelected = { onUpdateGestureAction(GestureType.PROXIMITY_WAVE, it) },
                onTestAction = onTestAction
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GestureSectionHeader(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
