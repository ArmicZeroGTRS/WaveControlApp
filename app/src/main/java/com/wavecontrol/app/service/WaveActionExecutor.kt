package com.wavecontrol.app.service

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.KeyEvent
import com.wavecontrol.app.model.WaveAction
import com.wavecontrol.app.model.WaveSettings

class WaveActionExecutor(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

    private var toneGenerator: ToneGenerator? = null
    private var isTorchOn = false

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 75)
        } catch (e: Exception) {
            Log.w("WaveActionExecutor", "Could not initialize ToneGenerator: ${e.message}")
        }
    }

    fun execute(action: WaveAction, settings: WaveSettings): Boolean {
        if (action == WaveAction.NONE) return false

        try {
            // Provide Haptic Feedback
            if (settings.isVibrationEnabled) {
                triggerVibration()
            }

            // Provide Sound Feedback
            if (settings.isSoundFeedbackEnabled) {
                triggerSound()
            }

            return when (action) {
                WaveAction.MEDIA_PLAY_PAUSE -> sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                WaveAction.MEDIA_NEXT -> sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT)
                WaveAction.MEDIA_PREV -> sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS)

                WaveAction.VOLUME_UP -> {
                    audioManager?.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_SHOW_UI
                    )
                    true
                }

                WaveAction.VOLUME_DOWN -> {
                    audioManager?.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_SHOW_UI
                    )
                    true
                }

                WaveAction.VOLUME_MUTE -> {
                    audioManager?.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_TOGGLE_MUTE,
                        AudioManager.FLAG_SHOW_UI
                    )
                    true
                }

                WaveAction.TORCH_TOGGLE -> toggleTorch()
                WaveAction.TORCH_ON -> setTorchState(true)
                WaveAction.TORCH_OFF -> setTorchState(false)

                WaveAction.NONE -> false
            }
        } catch (e: Exception) {
            Log.e("WaveActionExecutor", "Error executing action $action: ${e.message}", e)
            return false
        }
    }

    private fun sendMediaKeyEvent(keyCode: Int): Boolean {
        val am = audioManager ?: return false
        return try {
            val down = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
            val up = KeyEvent(KeyEvent.ACTION_UP, keyCode)
            am.dispatchMediaKeyEvent(down)
            am.dispatchMediaKeyEvent(up)
            true
        } catch (e: Exception) {
            Log.e("WaveActionExecutor", "Failed to dispatch media key $keyCode: ${e.message}")
            false
        }
    }

    private fun toggleTorch(): Boolean {
        return setTorchState(!isTorchOn)
    }

    private fun setTorchState(enable: Boolean): Boolean {
        val cm = cameraManager ?: return false
        try {
            val cameraIds = cm.cameraIdList
            for (id in cameraIds) {
                val chars = cm.getCameraCharacteristics(id)
                val hasFlash = chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
                val facing = chars.get(CameraCharacteristics.LENS_FACING)
                if (hasFlash && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    cm.setTorchMode(id, enable)
                    isTorchOn = enable
                    return true
                }
            }
        } catch (e: Exception) {
            Log.e("WaveActionExecutor", "Failed to set torch mode $enable: ${e.message}")
        }
        return false
    }

    private fun triggerVibration() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vm?.defaultVibrator
                val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                vibrator?.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                val effect = VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE)
                v?.vibrate(effect)
            }
        } catch (_: Exception) {
        }
    }

    private fun triggerSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
        } catch (_: Exception) {
        }
    }

    fun release() {
        try {
            if (isTorchOn) {
                setTorchState(false)
            }
            toneGenerator?.release()
            toneGenerator = null
        } catch (_: Exception) {
        }
    }
}
