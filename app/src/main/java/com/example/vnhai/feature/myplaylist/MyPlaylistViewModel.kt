package com.example.vnhai.feature.myplaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vnhai.AppApplication
import com.example.vnhai.SharedPreferences
import com.example.vnhai.data.LocalAppRepository
import com.example.vnhai.data.local.LocalRepository
import com.example.vnhai.data.local.entity.PlaylistEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPlaylistViewModel(private val localAppRepository: LocalAppRepository): ViewModel(){

    private val _uiState = MutableStateFlow(MyPlaylistState())
    val uiState: StateFlow<MyPlaylistState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MyPlaylistEvent>()
    val event: SharedFlow<MyPlaylistEvent> = _event.asSharedFlow()

    fun processIntent(intent: MyPlaylistIntent)
    {
        when(intent){
            MyPlaylistIntent.ChangeLayout -> TODO()
            MyPlaylistIntent.RemoveFromPlaylist -> TODO()
            MyPlaylistIntent.RemovePlaylist -> TODO()
            MyPlaylistIntent.RenamePlaylist -> TODO()
            is MyPlaylistIntent.CreateNewPlaylist -> {
                viewModelScope.launch(Dispatchers.IO){
                    val user = localAppRepository.getCurrentUser(SharedPreferences(intent.context).userName)
                    val newPlaylist = PlaylistEntity(
                        name = uiState.value.newPlaylistName,
                        userId = user.id,
                    )
                    localAppRepository.insertPlaylist(newPlaylist)
                    _uiState.update { currentState ->
                        currentState.copy(isCreateNewVisible = !uiState.value.isCreateNewVisible)
                    }
                }
            }

            is MyPlaylistIntent.EnterNewPlaylistName -> {
                _uiState.update { currentState ->
                    currentState.copy(newPlaylistName = intent.newName)
                }
            }

            is MyPlaylistIntent.LoadListPlaylistWithSongs -> {
                viewModelScope.launch(Dispatchers.IO){
                    localAppRepository.getPlaylistWithSong().collect {
                        listPlaylistWithSongs ->
                        _uiState.update { currentState ->
                            currentState.copy(listPlaylistWithSongs = listPlaylistWithSongs)
                        }
                    }
                }
            }

            MyPlaylistIntent.ChangeCreateNewVisible -> {
                _uiState.update { currentState ->
                    currentState.copy(isCreateNewVisible = !uiState.value.isCreateNewVisible)
                }
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appLocalRepository = (this[APPLICATION_KEY] as AppApplication).container.localAppRepository
                MyPlaylistViewModel(appLocalRepository)
            }
        }
    }

}