package com.wavecontrol.app

import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveAction
import com.wavecontrol.app.model.WaveSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WaveModelTest {

    @Test
    fun defaultSettings_hasSensibleDefaults() {
        val settings = WaveSettings()
        assertEquals(WaveAction.MEDIA_PREV, settings.actionWaveLeft)
        assertEquals(WaveAction.MEDIA_NEXT, settings.actionWaveRight)
        assertEquals(WaveAction.VOLUME_UP, settings.actionWaveUp)
        assertEquals(WaveAction.VOLUME_DOWN, settings.actionWaveDown)
        assertEquals(WaveAction.MEDIA_PLAY_PAUSE, settings.actionHandHover)
        assertEquals(WaveAction.MEDIA_PLAY_PAUSE, settings.actionProximity)
        assertTrue(settings.pauseOnScreenOff)
        assertFalse(settings.swapAxes)
        assertFalse(settings.invertHorizontal)
        assertFalse(settings.invertVertical)

        // Gesture enablement toggles default to true
        assertTrue(settings.isWaveLeftEnabled)
        assertTrue(settings.isWaveRightEnabled)
        assertTrue(settings.isWaveUpEnabled)
        assertTrue(settings.isWaveDownEnabled)
        assertTrue(settings.isHandHoverEnabled)
        assertTrue(settings.isProximityActionEnabled)

        // Stitch Kinetic Spatial Settings defaults
        assertEquals("dark", settings.themeMode)
        assertEquals(0.68f, settings.hapticIntensity, 0.001f)
        assertFalse(settings.reducedMotion)
        assertEquals("CENTER_ISLAND", settings.hudAnchorPosition)
    }

    @Test
    fun getActionForGesture_returnsCorrectAction() {
        val settings = WaveSettings(
            actionWaveLeft = WaveAction.VOLUME_MUTE,
            actionWaveRight = WaveAction.TORCH_TOGGLE,
            actionWaveUp = WaveAction.VOLUME_UP,
            actionWaveDown = WaveAction.VOLUME_DOWN
        )

        assertEquals(WaveAction.VOLUME_MUTE, settings.getActionForGesture(GestureType.WAVE_LEFT))
        assertEquals(WaveAction.TORCH_TOGGLE, settings.getActionForGesture(GestureType.WAVE_RIGHT))
        assertEquals(WaveAction.VOLUME_UP, settings.getActionForGesture(GestureType.WAVE_UP))
        assertEquals(WaveAction.VOLUME_DOWN, settings.getActionForGesture(GestureType.WAVE_DOWN))
        assertEquals(WaveAction.NONE, settings.getActionForGesture(GestureType.NONE))
    }

    @Test
    fun getActionForGesture_whenDisabled_returnsNone() {
        val settings = WaveSettings(
            actionWaveLeft = WaveAction.MEDIA_PREV,
            actionWaveRight = WaveAction.MEDIA_NEXT,
            actionWaveUp = WaveAction.VOLUME_UP,
            actionWaveDown = WaveAction.VOLUME_DOWN,
            isWaveLeftEnabled = false,
            isWaveRightEnabled = true,
            isWaveUpEnabled = false,
            isWaveDownEnabled = true
        )

        // Wave Left is disabled -> returns NONE
        assertEquals(WaveAction.NONE, settings.getActionForGesture(GestureType.WAVE_LEFT))
        assertFalse(settings.isGestureEnabled(GestureType.WAVE_LEFT))

        // Wave Right is enabled -> returns MEDIA_NEXT
        assertEquals(WaveAction.MEDIA_NEXT, settings.getActionForGesture(GestureType.WAVE_RIGHT))
        assertTrue(settings.isGestureEnabled(GestureType.WAVE_RIGHT))

        // Wave Up is disabled -> returns NONE
        assertEquals(WaveAction.NONE, settings.getActionForGesture(GestureType.WAVE_UP))
        assertFalse(settings.isGestureEnabled(GestureType.WAVE_UP))

        // Wave Down is enabled -> returns VOLUME_DOWN
        assertEquals(WaveAction.VOLUME_DOWN, settings.getActionForGesture(GestureType.WAVE_DOWN))
        assertTrue(settings.isGestureEnabled(GestureType.WAVE_DOWN))
    }

    @Test
    fun safeActions_doNotRequireAccessibilityService() {
        // Zero actions require accessibility service to ensure 100% mobile banking safety
        WaveAction.values().forEach { action ->
            assertFalse("Action $action should not require accessibility", action.requiresAccessibility)
        }
    }
}
