package com.example.vnhai.feature.playlistSongs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vnhai.AppApplication
import com.example.vnhai.data.LocalAppRepository
import com.example.vnhai.feature.myplaylist.MyPlaylistEvent
import com.example.vnhai.feature.myplaylist.MyPlaylistState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlaylistSongsViewModel(val localRepository: LocalAppRepository): ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistSongsState())
    val uiState: StateFlow<PlaylistSongsState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<PlaylistSongsState>()
    val event: SharedFlow<PlaylistSongsState> = _event.asSharedFlow()

    fun processIntent(intent: PlaylistSongsIntent){
        when(intent){
            PlaylistSongsIntent.OnSongClick -> TODO()
            PlaylistSongsIntent.RemoveSongFromPlaylist -> TODO()
            is PlaylistSongsIntent.SetCurrentSong -> {
                _uiState.update { currentState ->
                    currentState.copy(currentSong = intent.song)
                }
            }
            is PlaylistSongsIntent.SetPlaylistWithSongs -> {
                _uiState.update { currentState ->
                    currentState.copy(playlistWithSongs = intent.playlistWithSongs)
                }
            }
        }
    }
    
    fun processEvent(event: PlaylistSongsEvent){

    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val localRepository = (this[APPLICATION_KEY] as AppApplication).container.localAppRepository
                PlaylistSongsViewModel(localRepository)
            }
        }
    }
}