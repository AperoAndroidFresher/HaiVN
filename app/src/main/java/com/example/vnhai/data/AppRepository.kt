package com.example.vnhai.data

import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.PlaylistSongCrossRef
import com.example.vnhai.data.local.entity.PlaylistWithSongs
import com.example.vnhai.data.local.entity.UserEntity
import com.example.vnhai.data.remote.model.MusicFRemote
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface LocalAppRepository {
    fun getListUser(): Flow<List<UserEntity>>

    fun getPlaylistWithSong(): Flow<List<PlaylistWithSongs>>

    fun getAllPlaylist(): Flow<List<PlaylistEntity>>

    fun getAllMusicByType(type: String): Flow<List<SongEntity>>

    fun getPlaylistByUser(userId: Int): Flow<List<PlaylistEntity>>

    suspend fun checkUserName(userName: String): Int

    suspend fun checkUserNamePassword(userName: String, password: String): Int

    suspend fun insertUser(user: UserEntity)

    suspend fun insertSongToPlaylist(playlistSongCrossRef: PlaylistSongCrossRef)

    suspend fun insertMusic(music: SongEntity)

    suspend fun insertPlaylist(playlist: PlaylistEntity)

    suspend fun updatePlaylist(playlist: PlaylistEntity)

    suspend fun deletePlaylist(playlist: PlaylistEntity)

    suspend fun deleteMusic(music: SongEntity)

    suspend fun deleteSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef)
}

interface RemoteAppRepository {
    suspend fun getMusicFRemote(): Response<List<MusicFRemote>>

    suspend fun getMusicFRemoteMp3(musicName: String): Response<ResponseBody>
}