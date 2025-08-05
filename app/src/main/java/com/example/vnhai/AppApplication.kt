package com.example.vnhai

import android.app.Application
import com.example.vnhai.data.AppContainer
import com.example.vnhai.data.AppDataContainer

class AppApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}