package com.tiyi.tiyi_app

import android.app.Application
import com.tiyi.tiyi_app.data.AppContainer

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}