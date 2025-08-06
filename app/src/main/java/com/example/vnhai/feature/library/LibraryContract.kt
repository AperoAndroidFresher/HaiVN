package com.example.vnhai.feature.library

import android.content.Context
import com.example.vnhai.data.local.entity.MusicEntity
import com.example.vnhai.data.remote.model.MusicFRemote

data class LibraryState(
    val isLocal: Boolean = true,
    val isVisiblePlaylist: Boolean = false,
    val listMusic: List<MusicEntity> = listOf<MusicEntity>(),
    val remoteListMusic: List<MusicFRemote> = listOf<MusicFRemote>(),
    val hasPermission: Boolean = false,
    val isPermissionDialogVisible: Boolean = false
)

sealed interface LibraryIntent{
    data class AddToPlaylist(val music: MusicEntity): LibraryIntent
    data class Share(val music: MusicEntity): LibraryIntent
    data class LoadData(val context: Context): LibraryIntent
    data class ChangeDirection(val isLocal: Boolean): LibraryIntent
    data class GetPermissionState(val context: Context): LibraryIntent
    data class ChangeVisiblePermissionDialog(val visible: Boolean): LibraryIntent
    data object ChangeVisiblePlaylist: LibraryIntent
    data object GetRemoteListMusic: LibraryIntent
}

sealed interface LibraryEvent
{
    data object AskPermission: LibraryEvent
}