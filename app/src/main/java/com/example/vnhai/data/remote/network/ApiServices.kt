package com.example.vnhai.data.remote.network

import com.example.vnhai.data.remote.model.MusicFRemote
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ApiServices {
    @GET("/techtrek/Remote_audio.json")
    suspend fun getMusicFRemote(): Response<List<MusicFRemote>>

    @Streaming
    @GET("/techtreck/{musicName}")
    suspend fun getMusicFRemoteMp3(@Path("musicName") musicName: String): Response<ResponseBody>
}