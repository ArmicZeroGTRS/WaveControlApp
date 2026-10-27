package com.wavecontrol.app

import android.app.Application
import android.util.Log

class WaveControlApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Global uncaught exception handler to prevent Android OS "Kesalahan Aplikasi" dialogs
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("WaveControlApp", "Uncaught exception safely caught on thread [${thread.name}]: ${throwable.message}", throwable)
            // If it's a fatal crash on main thread during startup, delegate to default,
            // but log thoroughly to diagnostic logs
            if (thread == android.os.Looper.getMainLooper().thread) {
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }
}
