package com.example.vnhai.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vnhai.data.local.entity.MusicEntity
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ManagerDao {
    @Query("SELECT * FROM user")
    fun getListUser(): Flow<List<UserEntity>>

    @Query("SELECT * FROM playlist")
    fun getAllPlaylist(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM music")
    fun getAllMusic(): Flow<List<MusicEntity>>

    @Query("SELECT * FROM music WHERE playlistId = :playlistId")
    fun getMusicByPlaylist(playlistId: Int): Flow<List<MusicEntity>>

    @Query("SELECT * FROM playlist WHERE userId = :userId" )
    fun getPlaylistByUser(userId: Int): Flow<List<PlaylistEntity>>

    @Query("SELECT COUNT (*) FROM user WHERE username = :userName")
    suspend fun checkUserName(userName: String): Int

    @Query("SELECT COUNT (*) FROM user WHERE username = :userName AND password = :password")
    suspend fun checkUsernamePassword(userName: String, password: String): Int

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Insert
    suspend fun insertMusic(vararg music: MusicEntity)

    @Insert
    suspend fun insertPlaylist(vararg playlist: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deleteMusic(music: MusicEntity)
}