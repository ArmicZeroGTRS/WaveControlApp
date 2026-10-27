package com.wavecontrol.app.model

data class WaveSettings(
    val isEnabled: Boolean = false,
    val isCameraWaveEnabled: Boolean = true,
    val isProximityWaveEnabled: Boolean = true,
    val isFloatingHudEnabled: Boolean = true,
    val isVibrationEnabled: Boolean = true,
    val isSoundFeedbackEnabled: Boolean = true,
    val isLowPowerMode: Boolean = false,
    val pauseOnScreenOff: Boolean = true, // Auto-pause detection when screen is off to preserve battery
    val swapAxes: Boolean = false,        // Swap X (Horizontal) and Y (Vertical) axes
    val invertHorizontal: Boolean = false, // Invert Left <-> Right direction
    val invertVertical: Boolean = false,   // Invert Up <-> Down direction
    val sensitivity: Float = 0.55f,        // 0.1f (less sensitive) to 0.95f (more sensitive)
    val cooldownMs: Long = 750L,          // 400ms to 2000ms

    // Stitch Kinetic Spatial Settings
    val themeMode: String = "dark",       // "dark", "light", "system"
    val hapticIntensity: Float = 0.68f,   // 0.0f to 1.0f
    val reducedMotion: Boolean = false,   // Suppress decorative particle drift to prioritize raw FPS
    val hudAnchorPosition: String = "CENTER_ISLAND", // "TOP_RIGHT", "CENTER_ISLAND", "DYNAMIC_EDGE"

    // Gesture Mapping
    val actionWaveLeft: WaveAction = WaveAction.MEDIA_PREV,
    val actionWaveRight: WaveAction = WaveAction.MEDIA_NEXT,
    val actionWaveUp: WaveAction = WaveAction.VOLUME_UP,
    val actionWaveDown: WaveAction = WaveAction.VOLUME_DOWN,
    val actionHandHover: WaveAction = WaveAction.MEDIA_PLAY_PAUSE,
    val actionProximity: WaveAction = WaveAction.MEDIA_PLAY_PAUSE,

    // Gesture Enablement Toggles
    val isWaveLeftEnabled: Boolean = true,
    val isWaveRightEnabled: Boolean = true,
    val isWaveUpEnabled: Boolean = true,
    val isWaveDownEnabled: Boolean = true,
    val isHandHoverEnabled: Boolean = true,
    val isProximityActionEnabled: Boolean = true
) {
    fun getActionForGesture(gesture: GestureType): WaveAction {
        return when (gesture) {
            GestureType.WAVE_LEFT -> if (isWaveLeftEnabled) actionWaveLeft else WaveAction.NONE
            GestureType.WAVE_RIGHT -> if (isWaveRightEnabled) actionWaveRight else WaveAction.NONE
            GestureType.WAVE_UP -> if (isWaveUpEnabled) actionWaveUp else WaveAction.NONE
            GestureType.WAVE_DOWN -> if (isWaveDownEnabled) actionWaveDown else WaveAction.NONE
            GestureType.HAND_HOVER -> if (isHandHoverEnabled) actionHandHover else WaveAction.NONE
            GestureType.PROXIMITY_WAVE -> if (isProximityActionEnabled) actionProximity else WaveAction.NONE
            GestureType.NONE -> WaveAction.NONE
        }
    }

    fun isGestureEnabled(gesture: GestureType): Boolean {
        return when (gesture) {
            GestureType.WAVE_LEFT -> isWaveLeftEnabled
            GestureType.WAVE_RIGHT -> isWaveRightEnabled
            GestureType.WAVE_UP -> isWaveUpEnabled
            GestureType.WAVE_DOWN -> isWaveDownEnabled
            GestureType.HAND_HOVER -> isHandHoverEnabled
            GestureType.PROXIMITY_WAVE -> isProximityActionEnabled
            GestureType.NONE -> false
        }
    }
}
