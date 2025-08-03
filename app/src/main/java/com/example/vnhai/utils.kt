package com.example.vnhai

fun String.onlyLetters() = all { it.isLetterOrDigit() }
fun String.isValidateEmail() = endsWith("@apero.vn") &&
        subSequence(0,lastIndexOf("@apero.vn")).all { it.isLowerCase() || it=='_' || it.isDigit() }

fun checkUserName(name: String): Boolean
{
    return listUser.all { user -> user.username != name.lowercase() }
}

fun convertFromMilliSecondToMinuteAndSecond(){

}