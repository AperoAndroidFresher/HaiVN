package com.example.vnhai.feature.signup

data class SignUpState(
    val username: String = "",
    val password: String = "",
    val confirm: String = "",
    val email: String = "",

    val userError: Boolean = false,
    val passwordError: Boolean = false,
    val confirmError: Boolean = false,
    val emailError: Boolean = false,

    val passwordVisible: Boolean = false,
    val confirmVisible: Boolean = false
)

sealed interface SignUpIntent{
    data class EnterUserName(val username: String): SignUpIntent
    data class EnterPassword(val password: String): SignUpIntent
    data class EnterConfirm(val confirm: String): SignUpIntent
    data class EnterEmail(val email: String): SignUpIntent

    data class ChangeUserError(val userError: Boolean): SignUpIntent
    data class ChangePasswordError(val passwordError: Boolean): SignUpIntent
    data class ChangeConfirmError(val confirmError: Boolean): SignUpIntent
    data class ChangeEmailError(val emailError: Boolean): SignUpIntent


    object VisiblePassword: SignUpIntent
    object VisibleConfirm: SignUpIntent

    object SignUp: SignUpIntent
}

sealed interface SignUpEvent{

}