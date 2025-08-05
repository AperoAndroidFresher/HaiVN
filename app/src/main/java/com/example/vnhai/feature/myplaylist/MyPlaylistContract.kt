package com.example.vnhai.feature.myplaylist

import android.content.ContentResolver
import com.example.vnhai.Music
import com.example.vnhai.Playlist

data class MyPlaylistState(
    val columnLayout: Boolean = true,
    val listMusic: List<Music> = listOf<Music>(),
    val listPlaylist: List<Playlist> = listOf<Playlist>()
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
