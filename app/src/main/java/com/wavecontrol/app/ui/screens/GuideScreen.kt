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
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GuideScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Intro Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Panduan Wave Control",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kendalikan HP Android Anda tanpa menyentuh layar menggunakan sapuan tangan di udara.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        // Section: 3 Tips Utama
        Text(
            text = "Kunci Deteksi Akurat",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        GuideItemCard(
            icon = Icons.Default.PanTool,
            iconTint = Color(0xFF3B82F6),
            title = "1. Jarak Tangan Ideal (20 - 50 cm)",
            desc = "Posisikan tangan Anda sekitar satu jengkal hingga setengah meter di depan kamera depan layar. Hindari meletakkan tangan terlalu dekat (< 10 cm)."
        )

        GuideItemCard(
            icon = Icons.Default.BrightnessHigh,
            iconTint = Color(0xFFF59E0B),
            title = "2. Pencahayaan Cukup",
            desc = "Kamera memerlukan pencahayaan ruangan yang cukup agar kontras gerakan tangan dapat dianalisis dengan presisi."
        )

        GuideItemCard(
            icon = Icons.Default.Speed,
            iconTint = Color(0xFF10B981),
            title = "3. Ayunan Tegas & Halus",
            desc = "Lambaikan telapak tangan melintasi pandangan kamera dengan gerakan mantap dalam waktu sekitar setengah detik."
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Section: Kasus Penggunaan
        Text(
            text = "Skenario Praktis",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        GuideItemCard(
            icon = Icons.Default.Headphones,
            iconTint = Color(0xFF8B5CF6),
            title = "Saat Memasak atau Olahraga",
            desc = "Tangan basah atau kotor terkena minyak/bumbu? Cukup lambaikan tangan untuk ganti lagu di Spotify atau YouTube Music tanpa mengotori layar HP."
        )

        GuideItemCard(
            icon = Icons.Default.PhoneAndroid,
            iconTint = Color(0xFFEC4899),
            title = "Menonton TikTok / Reels / Shorts",
            desc = "Letakkan HP di atas meja atau tripod, aktifkan fitur Gulir ke Bawah, dan sapukan tangan untuk berpindah ke video berikutnya tanpa memegang ponsel."
        )

        GuideItemCard(
            icon = Icons.Default.Lightbulb,
            iconTint = Color(0xFF06B6D4),
            title = "Sensor Jarak Cepat (Proximity)",
            desc = "Kibas tangan tepat di atas speaker atas layar HP untuk jeda lagu atau bangunkan layar dengan konsumsi daya minimal."
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GuideItemCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    desc: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
