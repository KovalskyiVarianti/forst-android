package com.example.forst_android.main

import android.app.Application
import com.example.forst_android.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration
import timber.log.Timber

@HiltAndroidApp
class ForstApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }
}