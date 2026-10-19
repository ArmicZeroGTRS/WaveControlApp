package com.wavecontrol.app.model

data class WaveSettings(
    val isEnabled: Boolean = false,
    val isCameraWaveEnabled: Boolean = true,
    val isProximityWaveEnabled: Boolean = true,
    val isFloatingHudEnabled: Boolean = true,
    val isVibrationEnabled: Boolean = true,
    val isSoundFeedbackEnabled: Boolean = true,
    val isLowPowerMode: Boolean = false,
    val sensitivity: Float = 0.5f, // 0.2f (less sensitive) to 0.9f (more sensitive)
    val cooldownMs: Long = 800L,   // 500ms to 2000ms

    // Gesture Mapping
    val actionWaveLeft: WaveAction = WaveAction.MEDIA_PREV,
    val actionWaveRight: WaveAction = WaveAction.MEDIA_NEXT,
    val actionWaveUp: WaveAction = WaveAction.VOLUME_UP,
    val actionWaveDown: WaveAction = WaveAction.VOLUME_DOWN,
    val actionHandHover: WaveAction = WaveAction.MEDIA_PLAY_PAUSE,
    val actionProximity: WaveAction = WaveAction.MEDIA_PLAY_PAUSE
) {
    fun getActionForGesture(gesture: GestureType): WaveAction {
        return when (gesture) {
            GestureType.WAVE_LEFT -> actionWaveLeft
            GestureType.WAVE_RIGHT -> actionWaveRight
            GestureType.WAVE_UP -> actionWaveUp
            GestureType.WAVE_DOWN -> actionWaveDown
            GestureType.HAND_HOVER -> actionHandHover
            GestureType.PROXIMITY_WAVE -> actionProximity
            GestureType.NONE -> WaveAction.NONE
        }
    }
}
