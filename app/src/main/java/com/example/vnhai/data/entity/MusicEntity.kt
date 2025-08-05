package com.example.vnhai.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "music",
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("playlistId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class MusicEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val playlistId: Int,
    val link: String,
    val name: String,
    val author: String,
    val duration: String,
)