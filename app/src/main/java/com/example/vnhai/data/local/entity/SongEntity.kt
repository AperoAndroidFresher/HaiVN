package com.example.vnhai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "song",
)
data class SongEntity (
    @PrimaryKey(autoGenerate = true) val songId: Int = 0,
    val link: String,
    val name: String,
    val author: String,
    val duration: String,
    val type: String,
)