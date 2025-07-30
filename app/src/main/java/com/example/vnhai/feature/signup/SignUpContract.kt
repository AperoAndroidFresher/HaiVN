package com.example.vnhai.feature.signup

import com.example.vnhai.User

data class SignUpState(
    val currentUser: User = User("", ""),
    val username: String = "",
    val password: String = "",
    val checked: Boolean = false,
    val passwordVisible: Boolean = false
)

sealed interface SignUpIntent{
    data class EnterUserName(val username: String): SignUpIntent
    data class EnterPassword(val password: String): SignUpIntent
    object CheckRemember: SignUpIntent
    object SignIn: SignUpIntent
    object SignUp: SignUpIntent
}

sealed interface SignUpEvent{
    object SignIn: SignUpEvent
    object SignUp: SignUpEvent
}