package com.example.vnhai

import android.graphics.PointF.length
import kotlinx.serialization.StringFormat

fun String.onlyLetters() = all { it.isLetterOrDigit() }
fun String.isValidateEmail() = endsWith("@apero.vn") && length > 9 &&
        subSequence(
            0,
            lastIndexOf("@apero.vn")
        ).all { it.isLowerCase() || it == '_' || it.isDigit() }

fun convertFromMilliSecondToMinuteAndSecond(duration: String): String {
    val duration = duration.toInt() / 1000
    val minutes = duration / 60
    val seconds = duration - minutes * 60
    return "$minutes : ${"%02d".format(seconds)}"
}