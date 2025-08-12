package com.example.vnhai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist",
    indices = [Index(value = ["name"], unique = true)]
)
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true) val playlistId: Int = 0,
    val name: String ,
    val userId: Int,
)