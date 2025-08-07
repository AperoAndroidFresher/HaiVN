package com.example.vnhai

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import androidx.core.content.edit

const val INTERNAL_STORAGE_DIR = ""

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

fun saveFileToInternalStorage(filesDir: String, fileName: String, content: String){
    val directory = File(filesDir, INTERNAL_STORAGE_DIR)
    if(!directory.exists()){
        directory.mkdir()
    }

    val file = File(directory, fileName)

    val fileOutputStream = FileOutputStream(file)
    fileOutputStream.write(content.toByteArray())
    fileOutputStream.close()
}

fun readFileFromInternalStorage(filesDir: String, filename: String){
    val directory = File(filesDir, INTERNAL_STORAGE_DIR)
    val file = File(directory, filename)

    val fileInputStream = FileInputStream(file)
    val inputStreamReader = InputStreamReader(fileInputStream)
    val bufferedReader = BufferedReader(inputStreamReader)

    val stringBuilder = StringBuilder()
    var line: String? = bufferedReader.readLine()

    while(line != null)
    {
        stringBuilder.append(line)
        line = bufferedReader.readLine()
    }

    val fileContent = stringBuilder.toString()
    bufferedReader.close()
    inputStreamReader.close()
    fileInputStream.close()
}

fun saveFileToExternalStorage(){

}

fun readFileFromExternalStorage(){

}

class SharedPreferences(context: Context)
{
    val sharedPreFile = "PREFERENCE_FILE_KEY"

    val editor = context.getSharedPreferences(sharedPreFile, Context.MODE_PRIVATE)

    var userName: String = ""
        get() = editor.getString("userName", "").toString()
        set(value){
            editor.edit { putString("userName", value) }
            field = value
        }

    var password: String = ""
        get() = editor.getString("password", "").toString()
        set(value) {
            editor.edit{putString("password", value)}
            field = value
        }
}