package com.wavecontrol.app.detector

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.wavecontrol.app.model.GestureType
import kotlin.math.abs

class CameraWaveDetector(
    private var sensitivity: Float = 0.55f,
    private var cooldownMs: Long = 750L,
    private var swapAxes: Boolean = false,
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
        val activeCells: Int,
        val spanX: Int,
        val spanY: Int
    )

    private val motionHistory = mutableListOf<MotionFrame>()

    private var lastTriggerTime: Long = 0L
    private var hoverStartTime: Long = 0L
    private var isHovering = false

    fun updateConfig(
        sensitivity: Float,
        cooldownMs: Long,
        swapAxes: Boolean = false,
        invertHorizontal: Boolean = false,
        invertVertical: Boolean = false
    ) {
        this.sensitivity = sensitivity.coerceIn(0.1f, 1.0f)
        this.cooldownMs = cooldownMs.coerceIn(300L, 3000L)
        this.swapAxes = swapAxes
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
            val bufferLimit = buffer.limit()

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
                    val lum = if (index in 0 until bufferLimit) {
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
                return
            }

            // Global Auto-Exposure / illumination shift compensation
            val globalShift = (sumCurrent - sumPrev) / totalCells

            // Dynamic noise cutoff based on sensitivity
            val noiseCutoff = 10f - (sensitivity * 4.5f) // ~5.5 to 9.5

            var totalMotionDiff = 0f
            var activeCells = 0
            var weightedScreenX = 0f
            var weightedScreenY = 0f

            var minGx = gridSize
            var maxGx = -1
            var minGy = gridSize
            var maxGy = -1

            for (gy in 0 until gridSize) {
                for (gx in 0 until gridSize) {
                    val cellIdx = gy * gridSize + gx
                    val rawLum = currentGrid[cellIdx]
                    val compensatedDiff = abs((rawLum - globalShift) - prevGrid[cellIdx])

                    if (compensatedDiff > noiseCutoff) {
                        // Track bounding box of motion cluster
                        if (gx < minGx) minGx = gx
                        if (gx > maxGx) maxGx = gx
                        if (gy < minGy) minGy = gy
                        if (gy > maxGy) maxGy = gy

                        // Normalized sensor coordinates (0.0 to 1.0)
                        val u = gx.toFloat() / (gridSize - 1f)
                        val v = gy.toFloat() / (gridSize - 1f)

                        // Map sensor coordinates into portrait screen coordinates:
                        // Front camera sensors on mobile devices are mounted in landscape.
                        val (normX, normY) = when (rotationDegrees) {
                            90 -> Pair(1f - v, u)
                            180 -> Pair(1f - u, 1f - v)
                            270 -> Pair(v, 1f - u)
                            else -> Pair(v, 1f - u)
                        }

                        // Mirror front camera horizontally for natural user perspective
                        var screenX = 1f - normX
                        var screenY = normY

                        // Swap axes if requested
                        if (swapAxes) {
                            val temp = screenX
                            screenX = screenY
                            screenY = temp
                        }

                        if (invertHorizontal) screenX = 1f - screenX
                        if (invertVertical) screenY = 1f - screenY

                        totalMotionDiff += compensatedDiff
                        activeCells++
                        weightedScreenX += screenX * compensatedDiff
                        weightedScreenY += screenY * compensatedDiff
                    }
                }
            }

            // Update baseline
            System.arraycopy(currentGrid, 0, prevGrid, 0, totalCells)

            val frameEnergy = if (activeCells > 0) {
                ((totalMotionDiff / (activeCells * 40f)) * 10f).coerceIn(0f, 100f)
            } else 0f

            // Hand cluster dimensions
            val spanX = if (maxGx >= minGx) maxGx - minGx + 1 else 0
            val spanY = if (maxGy >= minGy) maxGy - minGy + 1 else 0
            val avgDiffPerCell = if (activeCells > 0) totalMotionDiff / activeCells else 0f

            // --- Robust Hand Heuristics ---
            // 1. Hand size envelope: at 15-40cm, hand occupies ~10 to 180 cells (2.5% to 45% of screen).
            //    Reject < 10 (cords, specks, ambient noise) and > 185 (phone shake, whole-room lighting).
            // 2. Spatial 2D span: palm + fingers form a 2D blob with spanX >= 3 and spanY >= 3.
            //    Reject 1D thin objects (pens, wires, edges).
            // 3. Motion energy density: avgDiffPerCell >= 11f.
            // 4. Reject whole-frame camera motion (touching all 4 borders simultaneously with high count).
            val isWholeScenePan = activeCells > 120 && minGx == 0 && maxGx == gridSize - 1 && minGy == 0 && maxGy == gridSize - 1
            val isValidHandCandidate = activeCells in 10..185 &&
                    spanX >= 3 && spanY >= 3 &&
                    avgDiffPerCell >= 11.0f &&
                    !isWholeScenePan

            if (isValidHandCandidate) {
                val cx = (weightedScreenX / totalMotionDiff).coerceIn(0f, 1f)
                val cy = (weightedScreenY / totalMotionDiff).coerceIn(0f, 1f)

                motionHistory.add(
                    MotionFrame(
                        screenX = cx,
                        screenY = cy,
                        time = now,
                        energy = frameEnergy,
                        activeCells = activeCells,
                        spanX = spanX,
                        spanY = spanY
                    )
                )
            }

            // Sliding window of 600ms for natural hand wave gestures
            motionHistory.removeAll { now - it.time > 600L }

            // Live vector calculation for telemetry preview
            var liveDx = 0f
            var liveDy = 0f
            if (motionHistory.size >= 2) {
                val oldest = motionHistory.first()
                val newest = motionHistory.last()
                liveDx = newest.screenX - oldest.screenX
                liveDy = newest.screenY - oldest.screenY
            }
            onLiveMotion?.invoke(frameEnergy, liveDx, liveDy)

            // Honor cooldown period
            if (now - lastTriggerTime < cooldownMs) {
                return
            }

            // --- Evaluate Wave Gestures ---
            // Require at least 3 consecutive valid hand frames to eliminate 1-frame glitches
            if (motionHistory.size >= 3) {
                val oldest = motionHistory.first()
                val newest = motionHistory.last()
                val duration = newest.time - oldest.time

                if (duration in 90L..550L) {
                    val dx = newest.screenX - oldest.screenX
                    val dy = newest.screenY - oldest.screenY

                    val absDx = abs(dx)
                    val absDy = abs(dy)

                    // Movement threshold scaled by sensitivity
                    val moveThreshold = 0.22f - (sensitivity * 0.08f) // ~0.14 to 0.21

                    var detectedGesture = GestureType.NONE

                    // Check Horizontal Dominance (Wave Left / Right)
                    if (absDx > moveThreshold && absDx > absDy * 1.25f) {
                        // Validate trajectory consistency: intermediate frames should progress monotonically
                        val midIndex = motionHistory.size / 2
                        val midX = motionHistory[midIndex].screenX
                        val progressesCorrectly = if (dx > 0) {
                            midX >= oldest.screenX - 0.05f && newest.screenX >= midX - 0.05f
                        } else {
                            midX <= oldest.screenX + 0.05f && newest.screenX <= midX + 0.05f
                        }

                        if (progressesCorrectly) {
                            detectedGesture = if (dx > 0) {
                                GestureType.WAVE_RIGHT
                            } else {
                                GestureType.WAVE_LEFT
                            }
                        }
                    }
                    // Check Vertical Dominance (Wave Up / Down)
                    else if (absDy > moveThreshold && absDy > absDx * 1.25f) {
                        val midIndex = motionHistory.size / 2
                        val midY = motionHistory[midIndex].screenY
                        val progressesCorrectly = if (dy > 0) {
                            midY >= oldest.screenY - 0.05f && newest.screenY >= midY - 0.05f
                        } else {
                            midY <= oldest.screenY + 0.05f && newest.screenY <= midY + 0.05f
                        }

                        if (progressesCorrectly) {
                            // In screen coordinates: dy > 0 is downwards, dy < 0 is upwards
                            detectedGesture = if (dy > 0) {
                                GestureType.WAVE_DOWN
                            } else {
                                GestureType.WAVE_UP
                            }
                        }
                    }

                    if (detectedGesture != GestureType.NONE) {
                        val confidence = ((absDx.coerceAtLeast(absDy) / moveThreshold) * 0.6f + 0.4f).coerceIn(0f, 1f)
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
                                motionEnergy = frameEnergy,
                                timestamp = now
                            )
                        )
                        return
                    }
                }
            }

            // --- Evaluate Hand Hover / Hold (Hand held steadily in front of camera) ---
            if (activeCells in 18..160 && spanX >= 3 && spanY >= 3 && avgDiffPerCell >= 12f) {
                if (motionHistory.size >= 2) {
                    val oldest = motionHistory.first()
                    val newest = motionHistory.last()
                    val driftX = abs(newest.screenX - oldest.screenX)
                    val driftY = abs(newest.screenY - oldest.screenY)

                    // Low drift indicates intentional stationary hand hold
                    if (driftX < 0.12f && driftY < 0.12f) {
                        if (hoverStartTime == 0L) {
                            hoverStartTime = now
                        } else if (now - hoverStartTime >= 600L && !isHovering) {
                            isHovering = true
                            lastTriggerTime = now
                            motionHistory.clear()
                            onGestureDetected(
                                DetectionEvent(
                                    gesture = GestureType.HAND_HOVER,
                                    confidence = 0.95f,
                                    vectorX = 0f,
                                    vectorY = 0f,
                                    motionEnergy = frameEnergy,
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

        } catch (t: Throwable) {
            Log.e("CameraWaveDetector", "Error analyzing frame: ${t.message}", t)
        } finally {
            try {
                image.close()
            } catch (t: Throwable) {
                Log.w("CameraWaveDetector", "Error closing image proxy: ${t.message}")
            }
        }
    }
}
