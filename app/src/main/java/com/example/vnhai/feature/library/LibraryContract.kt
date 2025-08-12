package com.example.vnhai.feature.library

import android.content.Context
import com.example.vnhai.RemoteState
import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.data.remote.model.MusicFRemote

data class LibraryState(
    val isLocal: Boolean = true,
    val isVisiblePlaylist: Boolean = false,
    val listLocalMusic: List<SongEntity> = listOf<SongEntity>(),
    val listRemoteMusic: List<SongEntity> = listOf<SongEntity>(),
    val remoteListMusic: List<MusicFRemote> = listOf<MusicFRemote>(),
    val hasPermission: Boolean = false,
    val isPermissionDialogVisible: Boolean = false,
    val remoteState: RemoteState = RemoteState.Loading
)

sealed interface LibraryIntent{
    data class AddToPlaylist(val music: SongEntity): LibraryIntent
    data class Share(val music: SongEntity): LibraryIntent
    data class GetLocalListMusic(val context: Context): LibraryIntent
    data class ChangeDirection(val isLocal: Boolean): LibraryIntent
    data class GetPermissionState(val context: Context): LibraryIntent
    data class ChangeVisiblePermissionDialog(val visible: Boolean): LibraryIntent
    data class GetRemoteListMusic(val context: Context): LibraryIntent
    data object ChangeVisiblePlaylist: LibraryIntent

}

sealed interface LibraryEvent
{
    data object AskPermission: LibraryEvent
}