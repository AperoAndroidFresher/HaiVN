package com.example.vnhai.feature.library

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vnhai.Music
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LibraryViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(LibraryState())
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LibraryEvent>()
    val event: SharedFlow<LibraryEvent> = _event.asSharedFlow()

    fun processIntent(intent: LibraryIntent){
        when(intent)
        {
            is LibraryIntent.ChangeDirection -> {
                _uiState.update { currentState ->
                    currentState.copy(isLocal = intent.isLocal)
                }
            }

            is LibraryIntent.LoadData -> {
                val listMusic = mutableListOf<Music>()
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
                            val duration = it.getString(durationColumn)

                            listMusic.add(Music(data, title, artist, duration))
                        }
                    }
                }
                _uiState.update { currentState ->
                    currentState.copy(listMusic = listMusic)
                }
                println(_uiState.value.listMusic.size)
            }

            is LibraryIntent.AddToPlaylist -> TODO()
            is LibraryIntent.Share -> TODO()
        }
    }

    fun processEvent(event: LibraryEvent){

    }
}