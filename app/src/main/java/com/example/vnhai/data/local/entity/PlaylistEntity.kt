package com.example.vnhai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist",
)
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true) val playlistId: Int,
    val name: String,
    val image: Int,
    val userId: Int,
)