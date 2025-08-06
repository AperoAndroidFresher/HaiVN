package com.example.vnhai.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val image: Int,
    val userId: Int,
)