package com.example.vnhai.data

import android.content.Context

interface AppContainer {
    val appRepository: AppRepository
}


class AppDataContainer(private val context: Context) : AppContainer {
    override val appRepository: AppRepository by lazy {
        LocalAppRepository(AppDatabase.getDatabase(context).managerDao())
    }
}
