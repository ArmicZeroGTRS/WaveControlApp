package com.wavecontrol.app.detector

import com.wavecontrol.app.model.GestureType

data class DetectionEvent(
    val gesture: GestureType,
    val confidence: Float,
    val vectorX: Float = 0f,
    val vectorY: Float = 0f,
    val motionEnergy: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)
