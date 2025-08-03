package com.example.vnhai.feature.library

import android.content.Context
import com.example.vnhai.Music

data class LibraryState(
    val isLocal: Boolean = true,
    val listMusic: List<Music> = listOf<Music>()
)

sealed interface LibraryIntent{
    data class AddToPlaylist(val music: Music): LibraryIntent
    data class Share(val music: Music): LibraryIntent
    data class LoadData(val context: Context): LibraryIntent
    data class ChangeDirection(val isLocal: Boolean): LibraryIntent
}

sealed interface LibraryEvent
{
    data object AskPermission: LibraryEvent
}