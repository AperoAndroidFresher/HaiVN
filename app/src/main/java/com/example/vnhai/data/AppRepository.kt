package com.example.vnhai.data

import com.example.vnhai.data.local.entity.MusicEntity
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.UserEntity
import com.example.vnhai.data.remote.model.MusicFRemote
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AppRepository {
    fun getListUser(): Flow<List<UserEntity>>

    fun getAllPlaylist(): Flow<List<PlaylistEntity>>

    fun getAllMusic(): Flow<List<MusicEntity>>

    fun getMusicByPlaylist(playlistId: Int): Flow<List<MusicEntity>>

    fun getPlaylistByUser(userId: Int): Flow<List<PlaylistEntity>>

    suspend fun checkUserName(userName: String): Int

    suspend fun checkUserNamePassword(userName: String, password: String): Int

    suspend fun insertUser(user: UserEntity)

    suspend fun insertMusic(vararg music: MusicEntity)

    suspend fun insertPlaylist(vararg playlist: PlaylistEntity)

    suspend fun updatePlaylist(playlist: PlaylistEntity)

    suspend fun deletePlaylist(playlist: PlaylistEntity)

    suspend fun deleteMusic(music: MusicEntity)
}

interface RemoteAppRepository {
    suspend fun getMusicFRemote(): Response<List<MusicFRemote>>
}