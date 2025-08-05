package com.example.vnhai.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vnhai.data.entity.MusicEntity
import com.example.vnhai.data.entity.PlaylistEntity
import com.example.vnhai.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getListUser(): Flow<List<UserEntity>>

    fun getAllPlaylist(): Flow<List<PlaylistEntity>>

    fun getAllMusic(): Flow<List<MusicEntity>>

    fun getMusicByPlaylist(playlistId: Int): Flow<List<MusicEntity>>

    fun getPlaylistByUser(userId: Int): Flow<List<PlaylistEntity>>

    suspend fun insertUser(user: UserEntity): Long

    suspend fun insertMusic(vararg music: MusicEntity)

    suspend fun insertPlaylist(vararg playlist: PlaylistEntity)

    suspend fun updatePlaylist(playlist: PlaylistEntity)

    suspend fun deletePlaylist(playlist: PlaylistEntity)

    suspend fun deleteMusic(music: MusicEntity)
}