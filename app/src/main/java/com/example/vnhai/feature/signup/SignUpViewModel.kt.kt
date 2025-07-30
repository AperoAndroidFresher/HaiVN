package com.example.vnhai.feature.signup

import androidx.lifecycle.ViewModel
import com.example.vnhai.feature.signin.SignInEvent
import com.example.vnhai.feature.signin.SignInIntent
import com.example.vnhai.feature.signin.SignInState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel: ViewModel()
{
    private val _uiState = MutableStateFlow<SignInState>(SignInState())
    val uiState: StateFlow<SignInState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignInEvent>()
    val event: SharedFlow<SignInEvent> = _event.asSharedFlow()

    fun processIntent(intent: SignUpIntent)
    {

    }

    fun processEvent(event: SignUpEvent){

    }
}