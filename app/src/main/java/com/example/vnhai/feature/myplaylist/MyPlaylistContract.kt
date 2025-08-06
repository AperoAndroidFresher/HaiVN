package com.example.vnhai.feature.myplaylist

import com.example.vnhai.data.local.entity.MusicEntity
import com.example.vnhai.data.local.entity.PlaylistEntity


data class MyPlaylistState(
    val columnLayout: Boolean = true,
    val listMusic: List<MusicEntity> = listOf<MusicEntity>(),
    val listPlaylist: List<PlaylistEntity> = listOf<PlaylistEntity>()
)

sealed interface MyPlaylistIntent{
    data object LoadsListPlaylist: MyPlaylistIntent
    data object AddPlaylist: MyPlaylistIntent
    data object RemovePlaylist: MyPlaylistIntent
    data object RenamePlaylist: MyPlaylistIntent
    data object LoadsListMusic: MyPlaylistIntent
    data object RemoveFromPlaylist: MyPlaylistIntent
    data object ChangeLayout: MyPlaylistIntent
}

sealed interface MyPlaylistEvent{

}
