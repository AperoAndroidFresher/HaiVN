package com.example.vnhai.feature.signin

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
import com.example.vnhai.data.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: AppRepository): ViewModel(){
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

            is SignInIntent.SignIn -> {
                viewModelScope.launch {
                    if(repository.checkUserNamePassword(uiState.value.username, uiState.value.password) == 1){
                        SharedPreferences(intent.context).userName = uiState.value.username
                        SharedPreferences(intent.context).password = uiState.value.password
                        intent.onClick()
                    }
                    else
                    {
                        Toast.makeText(intent.context,"Ten dang nhap hoac mat khau khong dung", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            is SignInIntent.LoadData -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        username = SharedPreferences(intent.context).userName,
                        password = SharedPreferences(intent.context).password)
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

