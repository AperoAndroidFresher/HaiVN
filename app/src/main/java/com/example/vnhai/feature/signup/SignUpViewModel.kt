package com.example.vnhai.feature.signup

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
import com.example.vnhai.isValidateEmail
import com.example.vnhai.data.local.entity.UserEntity
import com.example.vnhai.onlyLetters
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: LocalAppRepository): ViewModel()
{
    private val _uiState = MutableStateFlow<SignUpState>(SignUpState())
    val uiState: StateFlow<SignUpState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignUpEvent>()
    val event: SharedFlow<SignUpEvent> = _event.asSharedFlow()

    fun processIntent(intent: SignUpIntent)
    {
        when(intent)
        {
            is SignUpIntent.EnterConfirm -> {
                _uiState.update { currentState ->
                    currentState.copy(confirm = intent.confirm)
                }
            }

            is SignUpIntent.EnterEmail -> {
                _uiState.update { currentState ->
                    currentState.copy(email = intent.email)
                }
            }

            is SignUpIntent.EnterPassword -> {
                _uiState.update { currentState ->
                    currentState.copy(password = intent.password)
                }
            }

            is SignUpIntent.EnterUserName -> {
                _uiState.update { currentState ->
                    currentState.copy(username = intent.username)
                }
            }

            is SignUpIntent.SignUp -> {
                if (!uiState.value.username.onlyLetters() || uiState.value.username.isEmpty()) {
                    _uiState.update { currentState ->
                        currentState.copy(userError = true)
                    }
                }

                if (!uiState.value.password.onlyLetters() || uiState.value.password.isEmpty()) {
                    _uiState.update { currentState ->
                        currentState.copy(passwordError = true)
                    }
                }

                if (!uiState.value.confirm.onlyLetters() || uiState.value.confirm != uiState.value.password || uiState.value.confirm.isEmpty()) {
                    _uiState.update { currentState ->
                        currentState.copy(confirmError = true)
                    }
                }

                if (!uiState.value.email.isValidateEmail()) {
                    _uiState.update { currentState ->
                        currentState.copy(emailError = true)
                    }
                }

                if(!(uiState.value.userError || uiState.value.passwordError || uiState.value.confirmError || uiState.value.emailError))
                {
                    viewModelScope.launch {
                        if(repository.checkUserName(uiState.value.username.lowercase()) == 0)
                        {
                            repository.insertUser(UserEntity(
                                username = uiState.value.username,
                                password = uiState.value.password,
                                phone = "",
                                email = uiState.value.email,
                                name = "",
                                universityName = "",
                                description = "",
                            ))
                            intent.onClick()
                            SharedPreferences(intent.context).userName = uiState.value.username
                            SharedPreferences(intent.context).password = uiState.value.password
                        }
                        else
                        {
                            Toast.makeText(intent.context,"Ten dang nhap da ton tai", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            SignUpIntent.VisibleConfirm -> {
                _uiState.update { currentState ->
                    currentState.copy(confirmVisible = !uiState.value.confirmVisible)
                }
            }

            SignUpIntent.VisiblePassword -> {
                _uiState.update { currentState ->
                    currentState.copy(passwordVisible = !uiState.value.passwordVisible)
                }
            }

            is SignUpIntent.ChangeConfirmError -> {
                _uiState.update { currentState ->
                    currentState.copy(confirmError = intent.confirmError)
                }
            }
            is SignUpIntent.ChangeEmailError -> {
                _uiState.update { currentState ->
                    currentState.copy(emailError = intent.emailError)
                }
            }
            is SignUpIntent.ChangePasswordError -> {
                _uiState.update { currentState ->
                    currentState.copy(passwordError = intent.passwordError)
                }
            }
            is SignUpIntent.ChangeUserError -> {
                _uiState.update { currentState ->
                    currentState.copy(userError = intent.userError)
                }
            }

        }
    }

    fun processEvent(event: SignUpEvent){
        when(event){
            is SignUpEvent.SignUpError -> {
                Toast.makeText(event.context,"Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = (this[APPLICATION_KEY] as AppApplication).container.localAppRepository
                SignUpViewModel(
                    repository = repository
                )
            }
        }
    }
}
