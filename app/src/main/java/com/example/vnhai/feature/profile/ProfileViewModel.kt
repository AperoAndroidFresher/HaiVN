package com.example.vnhai.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vnhai.AppApplication
import com.example.vnhai.data.AppRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(private val repository: AppRepository): ViewModel(){
    private val _uiState = MutableStateFlow<ProfileState>(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event: SharedFlow<ProfileEvent> = _event.asSharedFlow()

    fun processIntent(intent: ProfileIntent) {
        when(intent) {
            is ProfileIntent.EnterDescription -> {
                _uiState.update { currentState ->
                    currentState.copy(description = intent.description)
                }
            }

            is ProfileIntent.EnterName -> {
                _uiState.update { currentState ->
                    currentState.copy(name = intent.name)
                }
            }

            is ProfileIntent.EnterPhone -> {
                _uiState.update { currentState ->
                    currentState.copy(phone = intent.phone)
                }
            }

            is ProfileIntent.EnterUniversity -> {
                _uiState.update { currentState ->
                    currentState.copy(universityName = intent.universityName)
                }
            }

            is ProfileIntent.ChangeVisibleDialog -> {
                _uiState.update { currentState ->
                    currentState.copy(visibleDialog = intent.visibleDialog)
                }
            }

            ProfileIntent.ChangeVisibleIcon -> {
                _uiState.update { currentState ->
                    currentState.copy(visibleIcon = !uiState.value.visibleIcon)
                }
            }

            is ProfileIntent.HasNameError -> {
                _uiState.update { currentState ->
                    currentState.copy(hasNameError = intent.nameError)
                }
            }
            is ProfileIntent.HasUniversityNameError -> {
                _uiState.update { currentState ->
                    currentState.copy(hasUniversityNameError = intent.universityNameError)
                }
            }
        }
    }
    fun processEvent(event: ProfileEvent){

    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as AppApplication).container.appRepository
                ProfileViewModel(
                    repository = repository
                )
            }
        }
    }
}