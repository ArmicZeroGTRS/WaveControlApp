package com.wavecontrol.app.model

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WaveSettingsRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<WaveSettings> = _settingsFlow.asStateFlow()

    fun getSettings(): WaveSettings = _settingsFlow.value

    private fun loadSettings(): WaveSettings {
        return WaveSettings(
            isEnabled = prefs.getBoolean(KEY_IS_ENABLED, false),
            isCameraWaveEnabled = prefs.getBoolean(KEY_IS_CAMERA_ENABLED, true),
            isProximityWaveEnabled = prefs.getBoolean(KEY_IS_PROXIMITY_ENABLED, true),
            isFloatingHudEnabled = prefs.getBoolean(KEY_IS_FLOATING_HUD_ENABLED, true),
            isVibrationEnabled = prefs.getBoolean(KEY_IS_VIBRATION_ENABLED, true),
            isSoundFeedbackEnabled = prefs.getBoolean(KEY_IS_SOUND_ENABLED, true),
            isLowPowerMode = prefs.getBoolean(KEY_IS_LOW_POWER_MODE, false),
            pauseOnScreenOff = prefs.getBoolean(KEY_PAUSE_ON_SCREEN_OFF, true),
            swapAxes = prefs.getBoolean(KEY_SWAP_AXES, false),
            invertHorizontal = prefs.getBoolean(KEY_INVERT_HORIZONTAL, false),
            invertVertical = prefs.getBoolean(KEY_INVERT_VERTICAL, false),
            sensitivity = prefs.getFloat(KEY_SENSITIVITY, 0.55f),
            cooldownMs = prefs.getLong(KEY_COOLDOWN_MS, 750L),
            themeMode = prefs.getString(KEY_THEME_MODE, "dark") ?: "dark",
            hapticIntensity = prefs.getFloat(KEY_HAPTIC_INTENSITY, 0.68f),
            reducedMotion = prefs.getBoolean(KEY_REDUCED_MOTION, false),
            hudAnchorPosition = prefs.getString(KEY_HUD_ANCHOR_POSITION, "CENTER_ISLAND") ?: "CENTER_ISLAND",
            actionWaveLeft = getAction(KEY_ACTION_WAVE_LEFT, WaveAction.MEDIA_PREV),
            actionWaveRight = getAction(KEY_ACTION_WAVE_RIGHT, WaveAction.MEDIA_NEXT),
            actionWaveUp = getAction(KEY_ACTION_WAVE_UP, WaveAction.VOLUME_UP),
            actionWaveDown = getAction(KEY_ACTION_WAVE_DOWN, WaveAction.VOLUME_DOWN),
            actionHandHover = getAction(KEY_ACTION_HAND_HOVER, WaveAction.MEDIA_PLAY_PAUSE),
            actionProximity = getAction(KEY_ACTION_PROXIMITY, WaveAction.MEDIA_PLAY_PAUSE)
        )
    }

    fun updateSettings(update: (WaveSettings) -> WaveSettings) {
        val newSettings = update(_settingsFlow.value)
        _settingsFlow.value = newSettings
        saveSettings(newSettings)
    }

    private fun saveSettings(s: WaveSettings) {
        prefs.edit()
            .putBoolean(KEY_IS_ENABLED, s.isEnabled)
            .putBoolean(KEY_IS_CAMERA_ENABLED, s.isCameraWaveEnabled)
            .putBoolean(KEY_IS_PROXIMITY_ENABLED, s.isProximityWaveEnabled)
            .putBoolean(KEY_IS_FLOATING_HUD_ENABLED, s.isFloatingHudEnabled)
            .putBoolean(KEY_IS_VIBRATION_ENABLED, s.isVibrationEnabled)
            .putBoolean(KEY_IS_SOUND_ENABLED, s.isSoundFeedbackEnabled)
            .putBoolean(KEY_IS_LOW_POWER_MODE, s.isLowPowerMode)
            .putBoolean(KEY_PAUSE_ON_SCREEN_OFF, s.pauseOnScreenOff)
            .putBoolean(KEY_SWAP_AXES, s.swapAxes)
            .putBoolean(KEY_INVERT_HORIZONTAL, s.invertHorizontal)
            .putBoolean(KEY_INVERT_VERTICAL, s.invertVertical)
            .putFloat(KEY_SENSITIVITY, s.sensitivity)
            .putLong(KEY_COOLDOWN_MS, s.cooldownMs)
            .putString(KEY_THEME_MODE, s.themeMode)
            .putFloat(KEY_HAPTIC_INTENSITY, s.hapticIntensity)
            .putBoolean(KEY_REDUCED_MOTION, s.reducedMotion)
            .putString(KEY_HUD_ANCHOR_POSITION, s.hudAnchorPosition)
            .putString(KEY_ACTION_WAVE_LEFT, s.actionWaveLeft.name)
            .putString(KEY_ACTION_WAVE_RIGHT, s.actionWaveRight.name)
            .putString(KEY_ACTION_WAVE_UP, s.actionWaveUp.name)
            .putString(KEY_ACTION_WAVE_DOWN, s.actionWaveDown.name)
            .putString(KEY_ACTION_HAND_HOVER, s.actionHandHover.name)
            .putString(KEY_ACTION_PROXIMITY, s.actionProximity.name)
            .apply()
    }

    private fun getAction(key: String, default: WaveAction): WaveAction {
        val name = prefs.getString(key, default.name) ?: default.name
        return try {
            WaveAction.valueOf(name)
        } catch (_: Exception) {
            default
        }
    }

    companion object {
        private const val PREFS_NAME = "wave_control_prefs"
        private const val KEY_IS_ENABLED = "is_enabled"
        private const val KEY_IS_CAMERA_ENABLED = "is_camera_enabled"
        private const val KEY_IS_PROXIMITY_ENABLED = "is_proximity_enabled"
        private const val KEY_IS_FLOATING_HUD_ENABLED = "is_floating_hud_enabled"
        private const val KEY_IS_VIBRATION_ENABLED = "is_vibration_enabled"
        private const val KEY_IS_SOUND_ENABLED = "is_sound_enabled"
        private const val KEY_IS_LOW_POWER_MODE = "is_low_power_mode"
        private const val KEY_PAUSE_ON_SCREEN_OFF = "pause_on_screen_off"
        private const val KEY_SWAP_AXES = "swap_axes"
        private const val KEY_INVERT_HORIZONTAL = "invert_horizontal"
        private const val KEY_INVERT_VERTICAL = "invert_vertical"
        private const val KEY_SENSITIVITY = "sensitivity"
        private const val KEY_COOLDOWN_MS = "cooldown_ms"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_HAPTIC_INTENSITY = "haptic_intensity"
        private const val KEY_REDUCED_MOTION = "reduced_motion"
        private const val KEY_HUD_ANCHOR_POSITION = "hud_anchor_position"
        private const val KEY_ACTION_WAVE_LEFT = "action_wave_left"
        private const val KEY_ACTION_WAVE_RIGHT = "action_wave_right"
        private const val KEY_ACTION_WAVE_UP = "action_wave_up"
        private const val KEY_ACTION_WAVE_DOWN = "action_wave_down"
        private const val KEY_ACTION_HAND_HOVER = "action_hand_hover"
        private const val KEY_ACTION_PROXIMITY = "action_proximity"

        @Volatile
        private var INSTANCE: WaveSettingsRepository? = null

        fun getInstance(context: Context): WaveSettingsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WaveSettingsRepository(context).also { INSTANCE = it }
            }
        }
    }
}
