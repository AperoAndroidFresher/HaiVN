package com.example.vnhai.data.local

import com.example.vnhai.data.LocalAppRepository
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.PlaylistSongCrossRef
import com.example.vnhai.data.local.entity.PlaylistWithSongs
import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class LocalRepository(private val managerDao: ManagerDao): LocalAppRepository {
    override fun getListUser(): Flow<List<UserEntity>> = managerDao.getListUser()

    override fun getPlaylistWithSong(): Flow<List<PlaylistWithSongs>> = managerDao.getPlaylistsWithSongs()

    override fun getCurrentUser(userName: String): UserEntity = managerDao.getCurrentUser(userName)

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

    override suspend fun insertMusic(music: SongEntity) = managerDao.insertMusic(music)

    override suspend fun insertPlaylist(playlist: PlaylistEntity) = managerDao.insertPlaylist(playlist)

    override suspend fun updatePlaylist(playlist: PlaylistEntity)  = managerDao.updatePlaylist(playlist)

    override suspend fun deletePlaylist(playlist: PlaylistEntity)  = managerDao.deletePlaylist(playlist)

    override suspend fun deleteMusic(music: SongEntity)  = managerDao.deleteMusic(music)

    override suspend fun deleteSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef) = managerDao.deleteSongFromPlaylist(playlistSongCrossRef)
}