package com.example.vnhai.feature.myplaylist

import android.content.Context
import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.data.local.entity.PlaylistEntity
import com.example.vnhai.data.local.entity.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow


data class MyPlaylistState(
    val columnLayout: Boolean = true,
    val listPlaylistWithSongs: List<PlaylistWithSongs> = listOf(),
    val isCreateNewVisible: Boolean = false,
    val isRenameVisible: Boolean = false,
    val newPlaylistName: String = "",
    val currentPlaylistWithSongs: PlaylistWithSongs = PlaylistWithSongs(PlaylistEntity(name = "", userId = 0), listOf())
)

sealed interface MyPlaylistIntent{
    data class LoadListPlaylistWithSongs(val context: Context): MyPlaylistIntent
    data class CreateNewPlaylist(val context: Context): MyPlaylistIntent
    data class EnterNewPlaylistName(val newName: String): MyPlaylistIntent
    data object ChangeCreateNewVisible: MyPlaylistIntent
    data object ChangeRenameVisible: MyPlaylistIntent
    data object RemovePlaylist: MyPlaylistIntent
    data class RenamePlaylist(val context: Context): MyPlaylistIntent
    data class SetCurrentPlaylistWithSongs(val currentPlaylistWithSongs: PlaylistWithSongs): MyPlaylistIntent

    data object RemoveFromPlaylist: MyPlaylistIntent
    data object ChangeLayout: MyPlaylistIntent
}

sealed interface MyPlaylistEvent{
}
