package com.angelina.lvivexplorer

import android.app.Application
import org.osmdroid.config.Configuration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().userAgentValue = packageName
    }
}
