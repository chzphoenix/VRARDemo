package com.huichongzi.vrardemo

import android.app.Application
import com.google.ar.core.ArCoreApk

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ArCoreApk.getInstance().checkAvailability(this)
    }
}