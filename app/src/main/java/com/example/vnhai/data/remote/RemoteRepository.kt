package com.example.vnhai.data.remote

import com.example.vnhai.data.RemoteAppRepository
import com.example.vnhai.data.remote.model.MusicFRemote
import com.example.vnhai.data.remote.network.ApiServices
import okhttp3.ResponseBody
import retrofit2.Response


class RemoteRepository(private val apiServices: ApiServices) : RemoteAppRepository{
    override suspend fun getMusicFRemote(): Response<List<MusicFRemote>> = apiServices.getMusicFRemote()

    override suspend fun getMusicFRemoteMp3(musicName: String): Response<ResponseBody> = apiServices.getMusicFRemoteMp3(musicName)
}