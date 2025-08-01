package com.example.vnhai.feature.library

import com.example.vnhai.Music

data class LibraryState(
    val isLocal: Boolean = true,
    val listMusic: List<Music> = listOf<Music>()
)

sealed interface LibraryIntent{
    data class AddToPlaylist(val music: Music): LibraryIntent
    data class Share(val music: Music): LibraryIntent
    data object LoadData: LibraryIntent
    data object ChangeDirection: LibraryIntent
}

sealed interface LibraryEvent
{
    data object AskPermission: LibraryEvent
}