package com.example.vnhai.feature.profile

data class ProfileState(
    val name: String = "",
    val phone: String = "",
    val universityName: String = "",
    val description: String = "",

    val visibleIcon: Boolean = true,
    val visibleDialog: Boolean = false,
    val hasNameError: Boolean = false,
    val hasUniversityNameError: Boolean = false,
)

sealed interface ProfileIntent{
    data class EnterName(val name: String): ProfileIntent
    data class EnterPhone(val phone: String): ProfileIntent
    data class EnterUniversity(val universityName: String): ProfileIntent
    data class EnterDescription(val description: String): ProfileIntent

    object ChangeVisibleIcon: ProfileIntent
    data class ChangeVisibleDialog(val visibleDialog: Boolean): ProfileIntent

    data class HasNameError(val nameError: Boolean): ProfileIntent
    data class HasUniversityNameError(val universityNameError: Boolean): ProfileIntent
}

sealed interface ProfileEvent{

}