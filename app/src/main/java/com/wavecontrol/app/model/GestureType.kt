package com.wavecontrol.app.model

enum class GestureType(
    val title: String,
    val description: String,
    val emoji: String
) {
    NONE("None", "No gesture detected yet", "⏳"),
    WAVE_LEFT("Wave Left", "Hand moving towards the left", "👈"),
    WAVE_RIGHT("Wave Right", "Hand moving towards the right", "👉"),
    WAVE_UP("Wave Up", "Hand moving upwards", "👆"),
    WAVE_DOWN("Wave Down", "Hand moving downwards", "👇"),
    HAND_HOVER("Hand Hover / Hold", "Holding palm still in front of camera", "✋"),
    PROXIMITY_WAVE("Proximity Wave", "Quick swipe near the screen sensor", "📱")
}
