package com.example.vnhai.data

import com.example.vnhai.data.entity.MusicEntity
import com.example.vnhai.data.entity.PlaylistEntity
import com.example.vnhai.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class LocalAppRepository(private val managerDao: ManagerDao): AppRepository {
    override fun getListUser(): Flow<List<UserEntity>> = managerDao.getListUser()

    override fun getAllPlaylist(): Flow<List<PlaylistEntity>>  = managerDao.getAllPlaylist()

    override fun getAllMusic(): Flow<List<MusicEntity>>  = managerDao.getAllMusic()

    override fun getMusicByPlaylist(playlistId: Int): Flow<List<MusicEntity>>  = managerDao.getMusicByPlaylist(playlistId)

    override fun getPlaylistByUser(userId: Int): Flow<List<PlaylistEntity>>  = managerDao.getPlaylistByUser(userId)

    override suspend fun insertUser(user: UserEntity)  = managerDao.insertUser(user)

    override suspend fun insertMusic(vararg music: MusicEntity)  = managerDao.insertMusic(*music)

    override suspend fun insertPlaylist(vararg playlist: PlaylistEntity)  = managerDao.insertPlaylist(*playlist)

    override suspend fun updatePlaylist(playlist: PlaylistEntity)  = managerDao.updatePlaylist(playlist)

    override suspend fun deletePlaylist(playlist: PlaylistEntity)  = managerDao.deletePlaylist(playlist)

    override suspend fun deleteMusic(music: MusicEntity)  = managerDao.deleteMusic(music)

}