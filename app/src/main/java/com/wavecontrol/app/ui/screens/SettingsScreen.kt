package com.wavecontrol.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wavecontrol.app.model.WaveSettings

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
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Sensitivity Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Kalibrasi & Sensitivitas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Sensitivity Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Kepekaan Gerakan:", style = MaterialTheme.typography.bodyMedium)
                    val label = when {
                        settings.sensitivity < 0.35f -> "Rendah"
                        settings.sensitivity < 0.7f -> "Normal"
                        else -> "Sangat Peka"
                    }
                    Text(
                        text = "$label (${(settings.sensitivity * 100).toInt()}%)",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp
                    )
                }

                Slider(
                    value = settings.sensitivity,
                    onValueChange = { newVal ->
                        onUpdateSettings { it.copy(sensitivity = newVal) }
                    },
                    valueRange = 0.1f..0.95f,
                    steps = 16,
                    colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Cooldown Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Jeda Antar Gerakan (Cooldown):", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "${String.format("%.1f", settings.cooldownMs / 1000f)} detik",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp
                    )
                }

                Slider(
                    value = settings.cooldownMs.toFloat(),
                    onValueChange = { newVal ->
                        onUpdateSettings { it.copy(cooldownMs = newVal.toLong()) }
                    },
                    valueRange = 400f..2000f,
                    steps = 15,
                    colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = "Mencegah gerakan terpicu dua kali secara tidak sengaja.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Features & Feedback Toggles Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Preferensi Fitur & Umpan Balik",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                SettingSwitchRow(
                    title = "Gunakan Kamera Depan",
                    subtitle = "Mendeteksi arah sapuan tangan (Kiri/Kanan/Atas/Bawah)",
                    checked = settings.isCameraWaveEnabled,
                    onCheckedChange = { onUpdateSettings { s -> s.copy(isCameraWaveEnabled = it) } }
                )

                SettingSwitchRow(
                    title = "Gunakan Sensor Jarak (Proximity)",
                    subtitle = "Mendeteksi kibasan cepat di depan layar HP",
                    checked = settings.isProximityWaveEnabled,
                    onCheckedChange = { onUpdateSettings { s -> s.copy(isProximityWaveEnabled = it) } }
                )

                SettingSwitchRow(
                    title = "Badge / HUD Mengambang",
                    subtitle = "Tampilkan ikon gerakan melayang di atas aplikasi lain",
                    checked = settings.isFloatingHudEnabled,
                    onCheckedChange = { onUpdateSettings { s -> s.copy(isFloatingHudEnabled = it) } }
                )

                SettingSwitchRow(
                    title = "Getaran Haptic Feedback",
                    subtitle = "Getar singkat saat gerakan tangan berhasil dikenali",
                    checked = settings.isVibrationEnabled,
                    onCheckedChange = { onUpdateSettings { s -> s.copy(isVibrationEnabled = it) } }
                )

                SettingSwitchRow(
                    title = "Suara Beep / Chime",
                    subtitle = "Nada konfirmasi saat perintah dijalankan",
                    checked = settings.isSoundFeedbackEnabled,
                    onCheckedChange = { onUpdateSettings { s -> s.copy(isSoundFeedbackEnabled = it) } }
                )

                SettingSwitchRow(
                    title = "Mode Hemat Daya",
                    subtitle = "Mengoptimalkan konsumsi baterai di latar belakang",
                    checked = settings.isLowPowerMode,
                    onCheckedChange = { onUpdateSettings { s -> s.copy(isLowPowerMode = it) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
