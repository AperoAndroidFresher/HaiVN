package com.example.vnhai.feature.playlistSongs

import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.PlaylistWithSongs
import com.example.vnhai.data.local.entity.SongEntity

data class PlaylistSongsState(
    val playlistWithSongs: PlaylistWithSongs = PlaylistWithSongs(PlaylistEntity(name = "", userId = 0), listOf()),
    val currentSong: SongEntity = SongEntity(link = "", name = "", author ="", duration = "", type = "")
)

sealed interface PlaylistSongsIntent{
    data class SetPlaylistWithSongs(val playlistWithSongs: PlaylistWithSongs): PlaylistSongsIntent
    data class SetCurrentSong(val song: SongEntity): PlaylistSongsIntent
    data object OnSongClick: PlaylistSongsIntent
    data object RemoveSongFromPlaylist: PlaylistSongsIntent
}

sealed interface PlaylistSongsEvent{

}