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
    fun safeActions_doNotRequireAccessibilityService() {
        // Zero actions require accessibility service to ensure 100% mobile banking safety
        WaveAction.values().forEach { action ->
            assertFalse("Action $action should not require accessibility", action.requiresAccessibility)
        }
    }
}
