package com.wavecontrol.app.service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import com.wavecontrol.app.model.GestureType
import com.wavecontrol.app.model.WaveAction

class FloatingOverlayManager(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    private val mainHandler = Handler(Looper.getMainLooper())

    private var overlayView: View? = null
    private var dismissRunnable: Runnable? = null

    val canDrawOverlays: Boolean
        get() = Settings.canDrawOverlays(context)

    @SuppressLint("InflateParams")
    fun showGestureFeedback(gesture: GestureType, action: WaveAction) {
        if (!canDrawOverlays || windowManager == null) return

        mainHandler.post {
            try {
                if (overlayView == null) {
                    val container = FrameLayout(context).apply {
                        setPadding(32, 20, 32, 20)
                        background = android.graphics.drawable.GradientDrawable().apply {
                            setColor(0xCC0F172A.toInt()) // Sleek slate dark background
                            cornerRadius = 48f
                            setStroke(2, 0x8038BDF8.toInt()) // Cyan glow border
                        }
                    }

                    val textView = TextView(context).apply {
                        id = View.generateViewId()
                        setTextColor(0xFFF8FAFC.toInt())
                        textSize = 14f
                        gravity = Gravity.CENTER
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }

                    container.addView(textView)

                    val layoutParams = WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        } else {
                            @Suppress("DEPRECATION")
                            WindowManager.LayoutParams.TYPE_PHONE
                        },
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT
                    ).apply {
                        gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                        y = 120 // Near top notch/camera
                    }

                    overlayView = container
                    windowManager.addView(container, layoutParams)
                }

                // Update text
                val container = overlayView as? FrameLayout
                val textView = container?.getChildAt(0) as? TextView
                textView?.text = "${gesture.emoji} ${gesture.title} → ${action.title}"

                overlayView?.alpha = 1.0f
                overlayView?.visibility = View.VISIBLE

                // Cancel previous hide
                dismissRunnable?.let { mainHandler.removeCallbacks(it) }

                // Schedule auto-hide
                val runnable = Runnable {
                    overlayView?.animate()
                        ?.alpha(0f)
                        ?.setDuration(300L)
                        ?.withEndAction {
                            overlayView?.visibility = View.GONE
                        }
                        ?.start()
                }
                dismissRunnable = runnable
                mainHandler.postDelayed(runnable, 1400L)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeOverlay() {
        mainHandler.post {
            try {
                dismissRunnable?.let { mainHandler.removeCallbacks(it) }
                if (overlayView != null) {
                    windowManager?.removeView(overlayView)
                    overlayView = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
