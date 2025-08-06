package com.example.vnhai.feature.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vnhai.AppApplication
import com.example.vnhai.data.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: AppRepository): ViewModel(){
    private val _uiState = MutableStateFlow<SignInState>(SignInState())
    val uiState: StateFlow<SignInState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignInEvent>()
    val event: SharedFlow<SignInEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val listUser = repository.getListUser().first()
            _uiState.update { currentState ->
                currentState.copy(listUser = listUser)
            }
            if(!listUser.isEmpty())
            {
                _uiState.update { currentState ->
                    currentState.copy(username = listUser.last().username, password = listUser.last().password)
                }
            }
        }
    }

    fun processIntent(intent: SignInIntent)
    {
        when(intent)
        {
            is SignInIntent.EnterUserName -> {
                _uiState.update { currentState ->
                    currentState.copy(username = intent.username)
                }
            }

            is SignInIntent.EnterPassword -> {
                _uiState.update { currentState ->
                    currentState.copy(password = intent.password)
                }
            }

            is SignInIntent.CheckRemember -> {
                _uiState.update { currentState ->
                    currentState.copy(checked = !uiState.value.checked)
                }
            }

            is SignInIntent.VisiblePassword -> {
                _uiState.update { currentState ->
                    currentState.copy(passwordVisible = !uiState.value.passwordVisible)
                }
            }

        }
    }

    fun processEvent(event: SignInEvent)
    {
        when(event)
        {
            is SignInEvent.SignIn -> {

            }
            is SignInEvent.SignUp -> {

            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as AppApplication).container.appRepository
                SignInViewModel(
                    repository = repository
                )
            }
        }
    }
}

