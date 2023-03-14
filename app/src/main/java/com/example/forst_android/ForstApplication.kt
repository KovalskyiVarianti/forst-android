package com.example.forst_android

import android.app.Application
import timber.log.Timber

class ForstApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}