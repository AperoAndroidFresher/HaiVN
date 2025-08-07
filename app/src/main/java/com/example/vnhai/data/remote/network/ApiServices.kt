package com.example.vnhai.data.remote.network

import com.example.vnhai.data.remote.model.MusicFRemote
import retrofit2.Response
import retrofit2.http.GET

interface ApiServices {
    @GET("/techtrek/Remote_audio.json")
    suspend fun getMusicFRemote(): Response<List<MusicFRemote>>
}