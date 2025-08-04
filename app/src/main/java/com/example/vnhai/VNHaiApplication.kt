package com.example.vnhai

import android.app.Application
import com.example.vnhai.model.AppDatabase

class VNHaiApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}