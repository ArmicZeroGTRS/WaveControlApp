package com.wavecontrol.app.detector

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.wavecontrol.app.model.GestureType
import kotlin.math.abs

class CameraWaveDetector(
    private var sensitivity: Float = 0.55f,
    private var cooldownMs: Long = 750L,
    private var invertHorizontal: Boolean = false,
    private var invertVertical: Boolean = false,
    private val onGestureDetected: (DetectionEvent) -> Unit,
    private val onLiveMotion: ((energy: Float, vectorX: Float, vectorY: Float) -> Unit)? = null
) : ImageAnalysis.Analyzer {

    private val gridSize = 20
    private val totalCells = gridSize * gridSize
    private val prevGrid = FloatArray(totalCells)
    private var hasPrev = false

    private data class MotionFrame(
        val screenX: Float,
        val screenY: Float,
        val time: Long,
        val energy: Float,
        val leftEnergy: Float,
        val rightEnergy: Float,
        val topEnergy: Float,
        val bottomEnergy: Float
    )

    private val motionHistory = mutableListOf<MotionFrame>()

    private var lastTriggerTime: Long = 0L
    private var hoverStartTime: Long = 0L
    private var isHovering = false

    fun updateConfig(
        sensitivity: Float,
        cooldownMs: Long,
        invertHorizontal: Boolean = false,
        invertVertical: Boolean = false
    ) {
        this.sensitivity = sensitivity.coerceIn(0.1f, 1.0f)
        this.cooldownMs = cooldownMs.coerceIn(300L, 3000L)
        this.invertHorizontal = invertHorizontal
        this.invertVertical = invertVertical
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
            val rotationDegrees = image.imageInfo.rotationDegrees

            val currentGrid = FloatArray(totalCells)

            // Step sizes in sensor coordinates
            val stepX = width / gridSize
            val stepY = height / gridSize

            var sumCurrent = 0f
            var sumPrev = 0f

            for (gy in 0 until gridSize) {
                val py = gy * stepY + stepY / 2
                val rowOffset = py * rowStride
                for (gx in 0 until gridSize) {
                    val px = gx * stepX + stepX / 2
                    val index = rowOffset + px * pixelStride
                    val lum = if (index < buffer.remaining()) {
                        (buffer.get(index).toInt() and 0xFF).toFloat()
                    } else {
                        0f
                    }
                    val cellIdx = gy * gridSize + gx
                    currentGrid[cellIdx] = lum
                    sumCurrent += lum
                    if (hasPrev) {
                        sumPrev += prevGrid[cellIdx]
                    }
                }
            }

            if (!hasPrev) {
                System.arraycopy(currentGrid, 0, prevGrid, 0, totalCells)
                hasPrev = true
                image.close()
                return
            }

            // Global Auto-Exposure / illumination shift compensation
            val globalShift = (sumCurrent - sumPrev) / totalCells

            // Dynamic noise cutoff based on sensitivity
            val noiseCutoff = 11f - (sensitivity * 5.5f) // ~5.5 to 10.5

            var totalMotionDiff = 0f
            var weightedScreenX = 0f
            var weightedScreenY = 0f

            var leftZoneEnergy = 0f
            var rightZoneEnergy = 0f
            var topZoneEnergy = 0f
            var bottomZoneEnergy = 0f

            for (gy in 0 until gridSize) {
                for (gx in 0 until gridSize) {
                    val cellIdx = gy * gridSize + gx
                    val rawLum = currentGrid[cellIdx]
                    val compensatedDiff = abs((rawLum - globalShift) - prevGrid[cellIdx])

                    if (compensatedDiff > noiseCutoff) {
                        // Normalized sensor coordinates (0.0 to 1.0)
                        val u = gx.toFloat() / (gridSize - 1f)
                        val v = gy.toFloat() / (gridSize - 1f)

                        // Transform sensor coords to screen portrait coords taking rotation into account
                        val (rotX, rotY) = when (rotationDegrees) {
                            90 -> Pair(1f - v, u)
                            180 -> Pair(1f - u, 1f - v)
                            270 -> Pair(v, 1f - u)
                            else -> Pair(u, v)
                        }

                        // Front camera is horizontally mirrored
                        var screenX = 1f - rotX
                        var screenY = rotY

                        if (invertHorizontal) screenX = 1f - screenX
                        if (invertVertical) screenY = 1f - screenY

                        totalMotionDiff += compensatedDiff
                        weightedScreenX += screenX * compensatedDiff
                        weightedScreenY += screenY * compensatedDiff

                        // Zone energy accumulations
                        if (screenX < 0.35f) leftZoneEnergy += compensatedDiff
                        else if (screenX > 0.65f) rightZoneEnergy += compensatedDiff

                        if (screenY < 0.35f) topZoneEnergy += compensatedDiff
                        else if (screenY > 0.65f) bottomZoneEnergy += compensatedDiff
                    }
                }
            }

            // Update baseline
            System.arraycopy(currentGrid, 0, prevGrid, 0, totalCells)

            val avgFrameEnergy = totalMotionDiff / totalCells
            val minEnergyThreshold = 5f - (sensitivity * 3f) // ~2.0 to 4.7

            if (avgFrameEnergy > minEnergyThreshold && totalMotionDiff > 0f) {
                val cx = (weightedScreenX / totalMotionDiff).coerceIn(0f, 1f)
                val cy = (weightedScreenY / totalMotionDiff).coerceIn(0f, 1f)

                motionHistory.add(
                    MotionFrame(
                        screenX = cx,
                        screenY = cy,
                        time = now,
                        energy = avgFrameEnergy,
                        leftEnergy = leftZoneEnergy,
                        rightEnergy = rightZoneEnergy,
                        topEnergy = topZoneEnergy,
                        bottomEnergy = bottomZoneEnergy
                    )
                )
            }

            // Keep sliding window of 350ms
            motionHistory.removeAll { now - it.time > 350L }

            // Live vector calculation for real-time preview UI
            var liveDx = 0f
            var liveDy = 0f
            if (motionHistory.size >= 2) {
                val oldest = motionHistory.first()
                val newest = motionHistory.last()
                liveDx = newest.screenX - oldest.screenX
                liveDy = newest.screenY - oldest.screenY
            }
            onLiveMotion?.invoke(avgFrameEnergy, liveDx, liveDy)

            // Honor cooldown period
            if (now - lastTriggerTime < cooldownMs) {
                image.close()
                return
            }

            // Evaluate Wave Gestures
            if (motionHistory.size >= 3) {
                val oldest = motionHistory.first()
                val newest = motionHistory.last()
                val duration = newest.time - oldest.time

                if (duration in 70L..380L) {
                    val dx = newest.screenX - oldest.screenX
                    val dy = newest.screenY - oldest.screenY

                    val absDx = abs(dx)
                    val absDy = abs(dy)

                    // Movement threshold based on sensitivity
                    val moveThreshold = 0.28f - (sensitivity * 0.13f) // ~0.15 to 0.25

                    var detectedGesture = GestureType.NONE

                    // Check Horizontal Dominance (Wave Left / Right)
                    if (absDx > moveThreshold && absDx > absDy * 1.25f) {
                        detectedGesture = if (dx > 0) {
                            GestureType.WAVE_RIGHT
                        } else {
                            GestureType.WAVE_LEFT
                        }
                    }
                    // Check Vertical Dominance (Wave Up / Down)
                    else if (absDy > moveThreshold && absDy > absDx * 1.25f) {
                        // Note: screenY = 0 is top of screen, screenY = 1 is bottom of screen
                        detectedGesture = if (dy > 0) {
                            GestureType.WAVE_DOWN // Moving towards bottom
                        } else {
                            GestureType.WAVE_UP   // Moving towards top
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
                                motionEnergy = avgFrameEnergy,
                                timestamp = now
                            )
                        )
                        image.close()
                        return
                    }
                }
            }

            // Evaluate Hand Hover / Hold (Hand held in central view without rapid translation)
            val hoverMinEnergy = 7f - (sensitivity * 3.5f)
            if (avgFrameEnergy > hoverMinEnergy) {
                if (motionHistory.size >= 2) {
                    val oldest = motionHistory.first()
                    val newest = motionHistory.last()
                    val driftX = abs(newest.screenX - oldest.screenX)
                    val driftY = abs(newest.screenY - oldest.screenY)

                    // If drift is low, hand is stationary
                    if (driftX < 0.15f && driftY < 0.15f) {
                        if (hoverStartTime == 0L) {
                            hoverStartTime = now
                        } else if (now - hoverStartTime >= 800L && !isHovering) {
                            isHovering = true
                            lastTriggerTime = now
                            motionHistory.clear()
                            onGestureDetected(
                                DetectionEvent(
                                    gesture = GestureType.HAND_HOVER,
                                    confidence = 0.95f,
                                    vectorX = 0f,
                                    vectorY = 0f,
                                    motionEnergy = avgFrameEnergy,
                                    timestamp = now
                                )
                            )
                        }
                    } else {
                        hoverStartTime = 0L
                    }
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
