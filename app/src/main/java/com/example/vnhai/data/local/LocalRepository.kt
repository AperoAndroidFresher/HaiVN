package com.example.vnhai.data.local

import android.util.Log
import com.example.vnhai.data.LocalAppRepository
import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.PlaylistSongCrossRef
import com.example.vnhai.data.local.entity.PlaylistWithSongs
import com.example.vnhai.data.local.entity.UserEntity
import com.example.vnhai.feature.myplaylist.listMusic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

class LocalRepository(private val managerDao: ManagerDao): LocalAppRepository {
    override fun getListUser(): Flow<List<UserEntity>> = managerDao.getListUser()
    override fun getPlaylistWithSong(): Flow<List<PlaylistWithSongs>> = managerDao.getPlaylistsWithSongs()

    override fun getAllPlaylist(): Flow<List<PlaylistEntity>> = managerDao.getAllPlaylist()

    override fun getAllMusicByType(type: String): Flow<List<SongEntity>> = managerDao.getAllMusicByType(type)

    override fun getPlaylistByUser(userId: Int): Flow<List<PlaylistEntity>> = managerDao.getPlaylistByUser(userId)
    override suspend fun checkUserName(userName: String): Int = managerDao.checkUserName(userName)

    override suspend fun checkUserNamePassword(
        userName: String,
        password: String
    ): Int = managerDao.checkUsernamePassword(userName, password)

    override suspend fun insertUser(user: UserEntity)  = managerDao.insertUser(user)

    override suspend fun insertSongToPlaylist(playlistSongCrossRef: PlaylistSongCrossRef) = managerDao.insertSongToPlaylist(playlistSongCrossRef)

    override suspend fun insertMusic(music: SongEntity) {
        var listLocalMusic: List<String> = managerDao.getAllMusicByType("local").last().map { it -> it.name }
        var listRemoteMusic: List<String> = managerDao.getAllMusicByType("remote").last().map { it -> it.name }

        if(!(listLocalMusic.contains(music.name) || listRemoteMusic.contains(music.name))){
            Log.d("main", "co chay la the deck nao")
            managerDao.insertMusic(music)
        }
    }

    override suspend fun insertPlaylist(playlist: PlaylistEntity)  = managerDao.insertPlaylist(playlist)

    override suspend fun updatePlaylist(playlist: PlaylistEntity)  = managerDao.updatePlaylist(playlist)

    override suspend fun deletePlaylist(playlist: PlaylistEntity)  = managerDao.deletePlaylist(playlist)

    override suspend fun deleteMusic(music: SongEntity)  = managerDao.deleteMusic(music)

    override suspend fun deleteSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef) = managerDao.deleteSongFromPlaylist(playlistSongCrossRef)
}