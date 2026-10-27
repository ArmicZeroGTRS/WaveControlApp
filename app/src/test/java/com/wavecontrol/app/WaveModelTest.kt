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
        assertFalse(settings.invertHorizontal)
        assertFalse(settings.invertVertical)
    }

    @Test
    fun getActionForGesture_returnsCorrectAction() {
        val settings = WaveSettings(
            actionWaveLeft = WaveAction.GLOBAL_BACK,
            actionWaveRight = WaveAction.SCROLL_DOWN,
            actionWaveUp = WaveAction.SCROLL_UP,
            actionWaveDown = WaveAction.GLOBAL_NOTIFICATIONS
        )

        assertEquals(WaveAction.GLOBAL_BACK, settings.getActionForGesture(GestureType.WAVE_LEFT))
        assertEquals(WaveAction.SCROLL_DOWN, settings.getActionForGesture(GestureType.WAVE_RIGHT))
        assertEquals(WaveAction.SCROLL_UP, settings.getActionForGesture(GestureType.WAVE_UP))
        assertEquals(WaveAction.GLOBAL_NOTIFICATIONS, settings.getActionForGesture(GestureType.WAVE_DOWN))
        assertEquals(WaveAction.NONE, settings.getActionForGesture(GestureType.NONE))
    }

    @Test
    fun accessibilityActions_requireAccessibilityService() {
        assertTrue(WaveAction.SCROLL_DOWN.requiresAccessibility)
        assertTrue(WaveAction.SCROLL_UP.requiresAccessibility)
        assertTrue(WaveAction.GLOBAL_BACK.requiresAccessibility)
        assertTrue(WaveAction.GLOBAL_HOME.requiresAccessibility)
        assertTrue(WaveAction.LOCK_SCREEN.requiresAccessibility)
    }
}
