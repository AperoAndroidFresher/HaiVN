package com.example.vnhai.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vnhai.User
import com.example.vnhai.isValidateEmail
import com.example.vnhai.model.dao.ManagerDao
import com.example.vnhai.model.entity.UserEntity
import com.example.vnhai.onlyLetters
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.String

class SignUpViewModel(private val managerDao: ManagerDao): ViewModel()
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

            SignUpIntent.SignUp -> {
                if (!uiState.value.username.onlyLetters()) {
                    _uiState.update { currentState ->
                        currentState.copy(userError = true)
                    }
                }

                if (!uiState.value.password.onlyLetters()) {
                    _uiState.update { currentState ->
                        currentState.copy(passwordError = true)
                    }
                }

                if (!uiState.value.confirm.onlyLetters() || uiState.value.confirm != uiState.value.password) {
                    _uiState.update { currentState ->
                        currentState.copy(confirmError = true)
                    }
                }

                if (!uiState.value.email.isValidateEmail()) {
                    _uiState.update { currentState ->
                        currentState.copy(emailError = true)
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

            SignUpIntent.SaveCurrentUser -> {
                managerDao.insertUser(UserEntity(
                    username = uiState.value.username,
                    password = uiState.value.password,
                    phone = "",
                    email = uiState.value.email,
                    name = "",
                    universityName = "",
                    description = "",
                ))
            }
        }
    }

    fun processEvent(event: SignUpEvent){

    }
}

class SignUpViewModelFactory(private val managerDao: ManagerDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(managerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}