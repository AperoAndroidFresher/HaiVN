package com.example.vnhai.data

import android.content.Context
import com.example.vnhai.data.local.AppDatabase
import com.example.vnhai.data.local.LocalRepository
import com.example.vnhai.data.remote.RemoteRepository
import com.example.vnhai.data.remote.network.ApiServices
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val appRepository: AppRepository
    val remoteAppRepository: RemoteAppRepository
}


class AppDataContainer(private val context: Context) : AppContainer {
    //For local
    override val appRepository: AppRepository by lazy {
        LocalRepository(AppDatabase.getDatabase(context).managerDao())
    }

    //For remote
    private val baseUrl = "https://static.apero.vn"
    private val requestTimeout = 30L

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(buildClient())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private fun buildClient(): OkHttpClient{
        return OkHttpClient().newBuilder()
            .connectTimeout(requestTimeout, TimeUnit.SECONDS)
            .readTimeout(requestTimeout, TimeUnit.SECONDS)
            .writeTimeout(requestTimeout, TimeUnit.SECONDS)
            .build()
    }

    private val retrofitService: ApiServices by lazy {
        retrofit.create(ApiServices::class.java)
    }

    override val remoteAppRepository: RemoteAppRepository by lazy {
        RemoteRepository(retrofitService)
    }
}
