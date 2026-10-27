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
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Security
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
                    text = "Wave Control User Guide",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Control your Android phone hands-free using air wave gestures in front of the front camera.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        // Section: Key Tips
        Text(
            text = "Key Detection Tips",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        GuideItemCard(
            icon = Icons.Default.PanTool,
            iconTint = Color(0xFF3B82F6),
            title = "1. Ideal Hand Distance (20 - 50 cm)",
            desc = "Position your hand roughly 20 to 50 cm in front of the front camera. Avoid holding your hand too close (< 10 cm)."
        )

        GuideItemCard(
            icon = Icons.Default.BrightnessHigh,
            iconTint = Color(0xFFF59E0B),
            title = "2. Adequate Ambient Lighting",
            desc = "The camera requires reasonable ambient room light to accurately calculate motion contrast vectors."
        )

        GuideItemCard(
            icon = Icons.Default.Speed,
            iconTint = Color(0xFF10B981),
            title = "3. Smooth & Steady Wave",
            desc = "Wave your palm across the camera's field of view in a steady, deliberate motion taking about 0.3 to 0.5 seconds."
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Section: Practical Scenarios
        Text(
            text = "Practical Scenarios & Safety",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        GuideItemCard(
            icon = Icons.Default.Headphones,
            iconTint = Color(0xFF8B5CF6),
            title = "While Cooking or Working Out",
            desc = "Wet or messy hands? Wave across to skip tracks on Spotify/YouTube, adjust volume, or pause playback without touching your device."
        )

        GuideItemCard(
            icon = Icons.Default.Security,
            iconTint = Color(0xFF10B981),
            title = "Mobile Banking & Android 15 Compliant",
            desc = "This application operates 100% free of Accessibility Services and Restricted Settings, ensuring zero interference with banking apps or Play Protect."
        )

        GuideItemCard(
            icon = Icons.Default.FlashlightOn,
            iconTint = Color(0xFFF59E0B),
            title = "Hands-Free Flashlight",
            desc = "Toggle the rear flashlight on or off with a wave when your hands are full or in the dark."
        )

        GuideItemCard(
            icon = Icons.Default.Lightbulb,
            iconTint = Color(0xFF06B6D4),
            title = "Quick Proximity Wave",
            desc = "Swipe directly across the top speaker sensor for instant play/pause control with minimal power draw."
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
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
