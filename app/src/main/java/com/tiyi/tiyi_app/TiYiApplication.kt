package com.tiyi.tiyi_app

import android.app.Application
import com.tiyi.tiyi_app.data.AppContainer

class TiYiApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}