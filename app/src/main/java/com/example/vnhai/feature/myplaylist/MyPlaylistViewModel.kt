package com.example.vnhai.feature.myplaylist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class MyPlaylistViewModel(): ViewModel(){

    private val _uiState = MutableStateFlow(MyPlaylistState())
    val uiState: StateFlow<MyPlaylistState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPlaylistEvent>()
    val event: SharedFlow<MyPlaylistEvent> = _event.asSharedFlow()

    fun processIntent(intent: MyPlaylistIntent)
    {
        when(intent){
            MyPlaylistIntent.AddPlaylist -> TODO()
            MyPlaylistIntent.ChangeLayout -> TODO()
            MyPlaylistIntent.LoadsListMusic -> TODO()
            MyPlaylistIntent.LoadsListPlaylist -> TODO()
            MyPlaylistIntent.RemoveFromPlaylist -> TODO()
            MyPlaylistIntent.RemovePlaylist -> TODO()
            MyPlaylistIntent.RenamePlaylist -> TODO()
        }
    }
}