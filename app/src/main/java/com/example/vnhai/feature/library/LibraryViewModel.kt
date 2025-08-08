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
import com.example.vnhai.convertFromMilliSecondToMinuteAndSecond
import com.example.vnhai.data.RemoteAppRepository
import com.example.vnhai.data.local.entity.MusicEntity
import com.example.vnhai.getNameMusicFromPath
import com.example.vnhai.saveFileToExternalStorage
import com.example.vnhai.saveFileToInternalStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(private val remoteAppRepository: RemoteAppRepository): ViewModel() {
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
                val listMusic = mutableListOf<MusicEntity>()
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

                            listMusic.add(MusicEntity(link = data, name = title,author = artist, duration = duration))
                        }
                    }
                }
                Log.d("main", "libraryviewmodel ${listMusic.size}")
                _uiState.update { currentState ->
                    currentState.copy(listMusic = listMusic)
                }
            }

            is LibraryIntent.AddToPlaylist -> TODO()
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
                viewModelScope.launch {
                    val response = remoteAppRepository.getMusicFRemote()
                    when{
                        response.isSuccessful -> {
                            val listMusic = response.body()
                            Log.d("main", "LibraryViewModel ${listMusic?.size}")
                            listMusic?.forEach { music ->
                                val responseMusic = remoteAppRepository.getMusicFRemoteMp3(getNameMusicFromPath(music.path))
                                when
                                {
                                    responseMusic.isSuccessful -> {
                                        Log.d("main", "LibraryViewModel ${responseMusic.body()}")
                                        val music = responseMusic.body()
                                        saveFileToInternalStorage(filesDir = intent.context.filesDir, fileName = "remote_music", content = music!!)
                                    }
                                    response.code() == 400 -> {Log.d("main", "LibraryViewModel bad request")}
                                    response.code() == 401 -> {Log.d("main", "LibraryViewModel unauthorized")}
                                    response.code() == 403 -> {Log.d("main", "LibraryViewModel forbidden")}
                                    response.code() == 404 -> {Log.d("main", "LibraryViewModel not found")}
                                    response.code() == 500 -> {Log.d("main", "LibraryViewModel internal server error")}
                                    else -> {
                                        Log.d("main", "LibraryViewModel eo biet")
                                    }
                                }

                            }
                        }
                        response.code() == 400 -> {}
                        response.code() == 401 -> {}
                        response.code() == 403 -> {}
                        response.code() == 404 -> {}
                        response.code() == 500 -> {}
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
                LibraryViewModel(appRemoteRepository)
            }
        }
    }
}

