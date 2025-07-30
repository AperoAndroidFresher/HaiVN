package com.example.vnhai.feature.signin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel(){
    private val _uiState = MutableStateFlow<SignInState>(SignInState())
    val uiState: StateFlow<SignInState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignInEvent>()
    val event: SharedFlow<SignInEvent> = _event.asSharedFlow()

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
}