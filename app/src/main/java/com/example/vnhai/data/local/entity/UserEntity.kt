package com.example.vnhai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String,
    val name: String,
    val phone: String,
    val email: String,
    val universityName: String,
    val description: String
)