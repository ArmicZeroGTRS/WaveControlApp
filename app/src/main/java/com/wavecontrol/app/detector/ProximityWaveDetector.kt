package com.wavecontrol.app.detector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.wavecontrol.app.model.GestureType

class ProximityWaveDetector(
    context: Context,
    private var cooldownMs: Long = 750L,
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
        isNear = false
        nearStartTime = 0L
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

        // Robust near check for binary (0/1) or analog distance
        val currentlyNear = if (maxRange <= 1.0f) {
            distance < 0.5f
        } else {
            distance < 4.0f && distance < maxRange
        }

        if (currentlyNear && !isNear) {
            // Hand enters proximity field
            isNear = true
            nearStartTime = now
        } else if (!currentlyNear && isNear) {
            // Hand exits proximity field
            isNear = false
            val duration = now - nearStartTime

            // A deliberate quick wave across sensor takes 50ms - 600ms
            // If held longer (> 700ms), it is an obstruction / pocket, so reject!
            if (duration in 50L..600L && (now - lastTriggerTime >= cooldownMs)) {
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
