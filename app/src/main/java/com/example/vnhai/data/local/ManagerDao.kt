package com.example.vnhai.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.PlaylistSongCrossRef
import com.example.vnhai.data.local.entity.PlaylistWithSongs
import com.example.vnhai.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ManagerDao {
    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getCurrentUser(username: String): UserEntity

    @Query("SELECT * FROM song WHERE name = :songName")
    fun getCurrentSong(songName: String): SongEntity

    @Query("SELECT * FROM playlist WHERE name = :playlistName")
    fun getCurrentPlaylist(playlistName: String): PlaylistEntity

    @Query("SELECT * FROM user")
    fun getListUser(): Flow<List<UserEntity>>

    @Query("SELECT * FROM playlist")
    fun getAllPlaylist(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM song WHERE type = :type")
    fun getAllMusicByType(type: String): Flow<List<SongEntity>>

    @Query("SELECT * FROM playlist WHERE userId = :userId" )
    fun getPlaylistByUser(userId: Int): Flow<List<PlaylistEntity>>

    @Query("SELECT COUNT (*) FROM user WHERE username = :userName")
    suspend fun checkUserName(userName: String): Int

    @Query("SELECT COUNT (*) FROM user WHERE username = :userName AND password = :password")
    suspend fun checkUsernamePassword(userName: String, password: String): Int

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMusic(music: SongEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongToPlaylist(playlistSongCrossRef: PlaylistSongCrossRef)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePlaylist(playlist: PlaylistEntity): Int

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deleteMusic(music: SongEntity)

    @Delete
    suspend fun deleteSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSongsOrder(playlistSongCrossRef: PlaylistSongCrossRef)
}