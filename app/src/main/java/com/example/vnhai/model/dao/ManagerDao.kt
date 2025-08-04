package com.example.vnhai.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vnhai.model.entity.MusicEntity
import com.example.vnhai.model.entity.PlaylistEntity
import com.example.vnhai.model.entity.UserEntity

@Dao
interface ManagerDao {
    @Query("SELECT * FROM user")
    fun getListUser(): List<UserEntity>

    @Query("SELECT * FROM playlist")
    fun getAllPlaylist(): List<PlaylistEntity>

    @Query("SELECT * FROM music")
    fun getAllMusic(): List<MusicEntity>

    @Query("SELECT * FROM music WHERE playlistId = :playlistId")
    fun getMusicByPlaylist(playlistId: Int): List<MusicEntity>

    @Query("SELECT * FROM playlist WHERE userId = :userId" )
    fun getPlaylistByUser(userId: Int): List<PlaylistEntity>

    @Insert
    fun insertUser(user: UserEntity)

    @Insert
    fun insertMusic(vararg music: MusicEntity)

    @Insert
    fun insertPlaylist(vararg playlist: PlaylistEntity)

    @Update
    fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    fun deletePlaylist(playlist: PlaylistEntity)

    @Delete
    fun deleteMusic(music: MusicEntity)
}