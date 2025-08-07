package com.example.vnhai.feature.signin

import android.content.Context
import com.example.vnhai.data.local.entity.UserEntity

data class SignInState(
    val username: String = "",
    val password: String = "",
    val checked: Boolean = false,
    val passwordVisible: Boolean = false,
    val listUser: List<UserEntity> = listOf()

)

sealed interface SignInIntent{
    data class EnterUserName(val username: String): SignInIntent
    data class EnterPassword(val password: String): SignInIntent
    data class LoadData(val context: Context): SignInIntent
    data class SignIn(val context: Context, val onClick: () -> Unit): SignInIntent

    object CheckRemember: SignInIntent
    object VisiblePassword: SignInIntent
}

sealed interface SignInEvent{
    object SignIn: SignInEvent
    object SignUp: SignInEvent
}