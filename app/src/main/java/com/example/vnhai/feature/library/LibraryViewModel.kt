package com.example.vnhai.feature.library

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vnhai.AppApplication
import com.example.vnhai.RemoteState
import com.example.vnhai.convertFromMilliSecondToMinuteAndSecond
import com.example.vnhai.data.LocalAppRepository
import com.example.vnhai.data.RemoteAppRepository
import com.example.vnhai.data.local.LocalRepository
import com.example.vnhai.data.local.entity.PlaylistSongCrossRef
import com.example.vnhai.data.local.entity.SongEntity
import com.example.vnhai.getNameMusicFromPath
import com.example.vnhai.saveFileToInternalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(private val remoteAppRepository: RemoteAppRepository,
    private val localAppRepository: LocalAppRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(LibraryState())
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LibraryEvent>()
    val event: SharedFlow<LibraryEvent> = _event.asSharedFlow()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun processIntent(intent: LibraryIntent){
        when(intent)
        {
            is LibraryIntent.ChangeDirection -> {
                _uiState.update { currentState ->
                    currentState.copy(isLocal = intent.isLocal)
                }
            }

            is LibraryIntent.GetLocalListMusic -> {
                viewModelScope.launch(Dispatchers.IO) {
                    localAppRepository.getAllMusicByType(type = "local").collect {
                            value ->
                        _uiState.update { currentState ->
                            currentState.copy(listLocalMusic = value)
                        }
                    }
                }

                if(uiState.value.isLocal)
                {
                    val contentResolver: ContentResolver = intent.context.contentResolver
                    val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    val selection = "${MediaStore.Audio.Media.DURATION} > 0"
                    val projection = arrayOf(
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DURATION
                    )

                    val cursor = contentResolver.query(uri, projection, selection, null, null)
                    cursor?.use{
                        val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                        val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                        val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                        val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                        val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                        while(it.moveToNext())
                        {
                            val id = it.getLong(idColumn)
                            val title = it.getString(titleColumn)
                            val artist = it.getString(artistColumn)
                            val data = it.getString(dataColumn)
                            val duration = convertFromMilliSecondToMinuteAndSecond(it.getString(durationColumn))

                            val song = SongEntity(
                                link = data,
                                name = title,
                                author = artist,
                                duration = duration,
                                type = "local"
                            )

                            viewModelScope.launch(Dispatchers.IO) {
                                localAppRepository.insertMusic(song)
                            }
                        }
                    }
                }
            }

            is LibraryIntent.AddToPlaylist -> {
                viewModelScope.launch(Dispatchers.IO) {
                    localAppRepository.insertSongToPlaylist(
                        PlaylistSongCrossRef(intent.playlistWithSongs.playlist.playlistId, uiState.value.currentSong.songId, intent.playlistWithSongs.songs.size+1)
                    )
                }
            }
            is LibraryIntent.Share -> TODO()
            LibraryIntent.ChangeVisiblePlaylist -> {
                _uiState.update { currentState ->
                    currentState.copy(isVisiblePlaylist = !uiState.value.isVisiblePlaylist)}
            }

            is LibraryIntent.GetPermissionState -> {
                val isGranted = ContextCompat.checkSelfPermission(intent.context,Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                if(isGranted)
                {
                    _uiState.update { currentState ->
                        currentState.copy(hasPermission = true, isPermissionDialogVisible = false)
                    }
                }
                else
                {
                    _uiState.update { currentState ->
                        currentState.copy(hasPermission = false, isPermissionDialogVisible = true)
                    }
                }
            }

            is LibraryIntent.ChangeVisiblePermissionDialog -> {
                _uiState.update { currentState ->
                    currentState.copy(isPermissionDialogVisible = intent.visible)
                }
            }

            is LibraryIntent.GetRemoteListMusic -> {
                _uiState.update { currentState ->
                    currentState.copy(remoteState = RemoteState.Loading)
                }

                viewModelScope.launch(Dispatchers.IO) {
                    localAppRepository.getAllMusicByType(type = "remote").collect {
                            value ->
                        _uiState.update { currentState ->
                            currentState.copy(listRemoteMusic = value)
                        }
                    }
                }

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val response = remoteAppRepository.getMusicFRemote()
                        when{
                            response.isSuccessful -> {
                                val listMusic = response.body()
                                listMusic?.map { music ->
                                    async {
                                        try {
                                            val responseMusic = remoteAppRepository.getMusicFRemoteMp3(getNameMusicFromPath(music.path))
                                            when
                                            {
                                                responseMusic.isSuccessful -> {
                                                    val mp3 = responseMusic.body()
                                                    val filePath = saveFileToInternalStorage(filesDir = intent.context.filesDir, fileName = "remote_music", content = mp3!!)
                                                    val song = SongEntity(
                                                        link = filePath,
                                                        name = music.title,
                                                        author = music.artist,
                                                        duration = convertFromMilliSecondToMinuteAndSecond(music.duration),
                                                        type = "remote"
                                                    )
                                                    localAppRepository.insertMusic(song)
                                                }
                                                else -> {
                                                    _uiState.update { currentState ->
                                                        currentState.copy(remoteState = RemoteState.Error)
                                                    }
                                                }
                                            }
                                        }catch (e: Exception)
                                        {
                                            _uiState.update { currentState ->
                                                currentState.copy(remoteState = RemoteState.Error)
                                            }
                                        }
                                    }
                                }?.awaitAll()
                                _uiState.update { currentState ->
                                    currentState.copy(remoteState = RemoteState.Success)
                                }
                            }
                            else -> {
                                _uiState.update { currentState ->
                                    currentState.copy(remoteState = RemoteState.Error)
                                }
                            }
                        }
                    }catch (e: Exception)
                    {
                        _uiState.update { currentState ->
                            currentState.copy(remoteState = RemoteState.Error)
                        }
                    }
                }
            }

            is LibraryIntent.SetCurrentSong -> {
                _uiState.update { currentState ->
                    currentState.copy(currentSong = intent.songs)
                }
            }

            is LibraryIntent.LoadListPlaylistWithSongs -> {
                viewModelScope.launch(Dispatchers.IO){
                    localAppRepository.getPlaylistWithSong().collect {
                            listPlaylistWithSongs ->
                        _uiState.update { currentState ->
                            currentState.copy(listPlaylistWithSongs = listPlaylistWithSongs)
                        }
                    }
                }
            }
        }
    }

    fun processEvent(event: LibraryEvent){

    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val appRemoteRepository = (this[APPLICATION_KEY] as AppApplication).container.remoteAppRepository
                val appLocalRepository = (this[APPLICATION_KEY] as AppApplication).container.localAppRepository
                LibraryViewModel(appRemoteRepository, appLocalRepository)
            }
        }
    }
}

