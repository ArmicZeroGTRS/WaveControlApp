package com.wavecontrol.app.detector

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.wavecontrol.app.model.GestureType
import java.nio.ByteBuffer
import kotlin.math.abs

class CameraWaveDetector(
    private var sensitivity: Float = 0.5f,
    private var cooldownMs: Long = 800L,
    private val onGestureDetected: (DetectionEvent) -> Unit,
    private val onLiveMotion: ((energy: Float, vectorX: Float, vectorY: Float) -> Unit)? = null
) : ImageAnalysis.Analyzer {

    private val gridW = 24
    private val gridH = 18
    private val prevGrid = FloatArray(gridW * gridH)
    private var hasPrev = false

    private data class MotionPoint(val x: Float, val y: Float, val time: Long, val energy: Float)
    private val motionHistory = mutableListOf<MotionPoint>()

    private var lastTriggerTime: Long = 0L
    private var hoverStartTime: Long = 0L
    private var isHovering = false

    fun updateConfig(sensitivity: Float, cooldownMs: Long) {
        this.sensitivity = sensitivity.coerceIn(0.1f, 1.0f)
        this.cooldownMs = cooldownMs.coerceIn(300L, 3000L)
    }

    override fun analyze(image: ImageProxy) {
        try {
            val now = System.currentTimeMillis()
            val plane = image.planes[0]
            val buffer = plane.buffer
            val rowStride = plane.rowStride
            val pixelStride = plane.pixelStride
            val width = image.width
            val height = image.height

            val currentGrid = FloatArray(gridW * gridH)

            // Downsample frame into grid of luminance averages
            val stepX = width / gridW
            val stepY = height / gridH

            for (gy in 0 until gridH) {
                val py = gy * stepY + stepY / 2
                val rowOffset = py * rowStride
                for (gx in 0 until gridW) {
                    val px = gx * stepX + stepX / 2
                    val index = rowOffset + px * pixelStride
                    val lum = if (index < buffer.remaining()) {
                        (buffer.get(index).toInt() and 0xFF).toFloat()
                    } else {
                        0f
                    }
                    currentGrid[gy * gridW + gx] = lum
                }
            }

            if (!hasPrev) {
                System.arraycopy(currentGrid, 0, prevGrid, 0, currentGrid.size)
                hasPrev = true
                image.close()
                return
            }

            // Calculate grid differences
            var totalDiff = 0f
            var weightedX = 0f
            var weightedY = 0f
            val noiseCutoff = 12f - (sensitivity * 6f) // Higher sensitivity = lower threshold

            for (i in currentGrid.indices) {
                val diff = abs(currentGrid[i] - prevGrid[i])
                if (diff > noiseCutoff) {
                    totalDiff += diff
                    val gx = i % gridW
                    val gy = i / gridW
                    weightedX += gx * diff
                    weightedY += gy * diff
                }
            }

            System.arraycopy(currentGrid, 0, prevGrid, 0, currentGrid.size)

            val avgEnergy = totalDiff / (gridW * gridH)
            val minEnergyThreshold = 6f - (sensitivity * 3.5f)

            if (avgEnergy > minEnergyThreshold) {
                val cx = (weightedX / totalDiff) / gridW // 0.0 to 1.0
                val cy = (weightedY / totalDiff) / gridH // 0.0 to 1.0
                motionHistory.add(MotionPoint(cx, cy, now, avgEnergy))
            }

            // Remove points older than 400ms
            motionHistory.removeAll { now - it.time > 400L }

            // Evaluate live vector for preview UI
            var liveVx = 0f
            var liveVy = 0f
            if (motionHistory.size >= 2) {
                val oldest = motionHistory.first()
                val newest = motionHistory.last()
                liveVx = newest.x - oldest.x
                liveVy = newest.y - oldest.y
            }
            onLiveMotion?.invoke(avgEnergy, liveVx, liveVy)

            // Check if in cooldown
            if (now - lastTriggerTime < cooldownMs) {
                image.close()
                return
            }

            // Evaluate gestures
            if (motionHistory.size >= 3) {
                val oldest = motionHistory.first()
                val newest = motionHistory.last()
                val timeSpan = newest.time - oldest.time

                if (timeSpan in 80L..400L) {
                    val dx = newest.x - oldest.x
                    val dy = newest.y - oldest.y

                    // Dynamic threshold based on sensitivity
                    val moveThreshold = 0.32f - (sensitivity * 0.16f) // ~0.16 to 0.28

                    val absDx = abs(dx)
                    val absDy = abs(dy)

                    var detectedGesture = GestureType.NONE

                    if (absDx > moveThreshold && absDx > absDy * 1.3f) {
                        // Horizontal wave
                        detectedGesture = if (dx > 0) {
                            GestureType.WAVE_RIGHT
                        } else {
                            GestureType.WAVE_LEFT
                        }
                    } else if (absDy > moveThreshold && absDy > absDx * 1.3f) {
                        // Vertical wave
                        detectedGesture = if (dy > 0) {
                            GestureType.WAVE_DOWN
                        } else {
                            GestureType.WAVE_UP
                        }
                    }

                    if (detectedGesture != GestureType.NONE) {
                        val confidence = ((absDx.coerceAtLeast(absDy) / moveThreshold) * 0.7f + 0.3f).coerceIn(0f, 1f)
                        lastTriggerTime = now
                        motionHistory.clear()
                        hoverStartTime = 0L
                        isHovering = false
                        onGestureDetected(
                            DetectionEvent(
                                gesture = detectedGesture,
                                confidence = confidence,
                                vectorX = dx,
                                vectorY = dy,
                                motionEnergy = avgEnergy,
                                timestamp = now
                            )
                        )
                        image.close()
                        return
                    }
                }
            }

            // Evaluate Hand Hover / Hold (Hand held in view with stable energy)
            val hoverMinEnergy = 8f - (sensitivity * 4f)
            if (avgEnergy > hoverMinEnergy) {
                if (hoverStartTime == 0L) {
                    hoverStartTime = now
                } else if (now - hoverStartTime >= 900L && !isHovering) {
                    isHovering = true
                    lastTriggerTime = now
                    onGestureDetected(
                        DetectionEvent(
                            gesture = GestureType.HAND_HOVER,
                            confidence = 0.95f,
                            vectorX = 0f,
                            vectorY = 0f,
                            motionEnergy = avgEnergy,
                            timestamp = now
                        )
                    )
                }
            } else {
                hoverStartTime = 0L
                isHovering = false
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            image.close()
        }
    }
}
