package com.example.vnhai.feature.myplaylist

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vnhai.AppApplication
import com.example.vnhai.SharedPreferences
import com.example.vnhai.data.LocalAppRepository
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
import kotlinx.coroutines.withContext

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
            MyPlaylistIntent.RemovePlaylist -> {
                viewModelScope.launch(Dispatchers.IO) {
                    localAppRepository.deletePlaylist(uiState.value.currentPlaylistWithSongs.playlist)
                }
            }

            is MyPlaylistIntent.RenamePlaylist -> {
                if(uiState.value.newPlaylistName.isNotEmpty())
                {
                    val newPlaylist = uiState.value.currentPlaylistWithSongs.playlist.copy(name = uiState.value.newPlaylistName)
                    viewModelScope.launch(Dispatchers.IO) {
                        val check = localAppRepository.updatePlaylist(newPlaylist)
                        if(check == 0)
                        {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(intent.context,"Playlist da ton tai, vui long thu lai", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else
                        {
                            _uiState.update { currentState ->
                                currentState.copy(isRenameVisible = false, newPlaylistName = uiState.value.currentPlaylistWithSongs.playlist.name)
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(intent.context,"Khong duoc de trong ten playlist", Toast.LENGTH_SHORT).show()
                }
            }

            is MyPlaylistIntent.CreateNewPlaylist -> {
                if(uiState.value.newPlaylistName.isNotEmpty()){
                    viewModelScope.launch(Dispatchers.IO){
                        val user = localAppRepository.getCurrentUser(SharedPreferences(intent.context).userName)
                        val newPlaylist = PlaylistEntity(
                            name = uiState.value.newPlaylistName,
                            userId = user.id,
                        )
                        val check = localAppRepository.insertPlaylist(newPlaylist)
                        if(check == -1L)
                        {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(intent.context,"Playlist da ton tai, vui long thu lai", Toast.LENGTH_SHORT).show()
                            }                        }
                        else
                        {
                            _uiState.update { currentState ->
                                currentState.copy(isCreateNewVisible = !uiState.value.isCreateNewVisible, newPlaylistName = "")
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(intent.context,"Khong duoc de trong ten playlist", Toast.LENGTH_SHORT).show()
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
                    currentState.copy(isCreateNewVisible = !uiState.value.isCreateNewVisible, newPlaylistName = "")
                }
            }

            MyPlaylistIntent.ChangeRenameVisible -> {
                _uiState.update { currentState ->
                currentState.copy(isRenameVisible = !uiState.value.isRenameVisible, newPlaylistName = uiState.value.currentPlaylistWithSongs.playlist.name)
                }
            }

            is MyPlaylistIntent.SetCurrentPlaylistWithSongs -> {
                _uiState.update { currentState ->
                    currentState.copy(currentPlaylistWithSongs = intent.currentPlaylistWithSongs)
                }
            }
        }
    }

    fun processEvent(event: MyPlaylistEvent)
    {

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