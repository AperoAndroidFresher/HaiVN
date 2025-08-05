package com.example.vnhai.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vnhai.data.entity.MusicEntity
import com.example.vnhai.data.entity.PlaylistEntity
import com.example.vnhai.data.entity.UserEntity
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

    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

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