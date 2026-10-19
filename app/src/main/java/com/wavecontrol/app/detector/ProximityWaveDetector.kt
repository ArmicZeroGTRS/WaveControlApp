package com.wavecontrol.app.detector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.wavecontrol.app.model.GestureType

class ProximityWaveDetector(
    context: Context,
    private var cooldownMs: Long = 800L,
    private val onGestureDetected: (DetectionEvent) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val proximitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    private var nearStartTime: Long = 0L
    private var lastTriggerTime: Long = 0L
    private var isNear = false
    private var isListening = false

    val isSupported: Boolean
        get() = proximitySensor != null

    fun updateCooldown(cooldownMs: Long) {
        this.cooldownMs = cooldownMs.coerceIn(300L, 3000L)
    }

    fun start() {
        if (isListening || proximitySensor == null) return
        sensorManager?.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_UI)
        isListening = true
    }

    fun stop() {
        if (!isListening) return
        sensorManager?.unregisterListener(this)
        isListening = false
        isNear = false
        nearStartTime = 0L
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_PROXIMITY) return

        val distance = event.values[0]
        val maxRange = event.sensor.maximumRange
        val now = System.currentTimeMillis()

        // Threshold for near: usually < 5cm or < maxRange
        val currentlyNear = distance < maxRange.coerceAtMost(5f)

        if (currentlyNear && !isNear) {
            // Hand approached
            isNear = true
            nearStartTime = now
        } else if (!currentlyNear && isNear) {
            // Hand moved away (wave completed)
            isNear = false
            val duration = now - nearStartTime

            // Quick wave: between 60ms and 700ms
            if (duration in 60L..700L && (now - lastTriggerTime >= cooldownMs)) {
                lastTriggerTime = now
                onGestureDetected(
                    DetectionEvent(
                        gesture = GestureType.PROXIMITY_WAVE,
                        confidence = 0.98f,
                        vectorX = 0f,
                        vectorY = 0f,
                        motionEnergy = 100f,
                        timestamp = now
                    )
                )
            }
            nearStartTime = 0L
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
