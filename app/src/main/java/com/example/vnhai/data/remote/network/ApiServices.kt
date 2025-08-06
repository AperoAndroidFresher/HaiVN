package com.example.vnhai.data.remote.network

import com.example.vnhai.data.remote.model.MusicFRemote
import retrofit2.Call
import retrofit2.http.GET

interface ApiServices {
    @GET()
    suspend fun getMusicFRemote(): Call<List<MusicFRemote>>
}