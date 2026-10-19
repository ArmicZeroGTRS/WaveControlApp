package com.wavecontrol.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveAction
import com.wavecontrol.app.model.WaveSettings
import com.wavecontrol.app.ui.components.GestureCard

@Composable
fun GestureConfigScreen(
    settings: WaveSettings,
    isAccessibilityEnabled: Boolean,
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
                    text = "Pemetaan Aksi Gerakan (Wave Mapping)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sesuaikan tindakan yang ingin dijalankan ketika Anda menggerakkan tangan di depan kamera atau sensor HP.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gesture Cards List
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GestureCard(
                gesture = GestureType.WAVE_LEFT,
                selectedAction = settings.actionWaveLeft,
                onActionSelected = { onUpdateGestureAction(GestureType.WAVE_LEFT, it) },
                onTestAction = onTestAction,
                isAccessibilityEnabled = isAccessibilityEnabled
            )

            GestureCard(
                gesture = GestureType.WAVE_RIGHT,
                selectedAction = settings.actionWaveRight,
                onActionSelected = { onUpdateGestureAction(GestureType.WAVE_RIGHT, it) },
                onTestAction = onTestAction,
                isAccessibilityEnabled = isAccessibilityEnabled
            )

            GestureCard(
                gesture = GestureType.WAVE_UP,
                selectedAction = settings.actionWaveUp,
                onActionSelected = { onUpdateGestureAction(GestureType.WAVE_UP, it) },
                onTestAction = onTestAction,
                isAccessibilityEnabled = isAccessibilityEnabled
            )

            GestureCard(
                gesture = GestureType.WAVE_DOWN,
                selectedAction = settings.actionWaveDown,
                onActionSelected = { onUpdateGestureAction(GestureType.WAVE_DOWN, it) },
                onTestAction = onTestAction,
                isAccessibilityEnabled = isAccessibilityEnabled
            )

            GestureCard(
                gesture = GestureType.HAND_HOVER,
                selectedAction = settings.actionHandHover,
                onActionSelected = { onUpdateGestureAction(GestureType.HAND_HOVER, it) },
                onTestAction = onTestAction,
                isAccessibilityEnabled = isAccessibilityEnabled
            )

            GestureCard(
                gesture = GestureType.PROXIMITY_WAVE,
                selectedAction = settings.actionProximity,
                onActionSelected = { onUpdateGestureAction(GestureType.PROXIMITY_WAVE, it) },
                onTestAction = onTestAction,
                isAccessibilityEnabled = isAccessibilityEnabled
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
