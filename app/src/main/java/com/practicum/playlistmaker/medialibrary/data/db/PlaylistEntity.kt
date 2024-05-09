package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val iconUrl: String,
    val name: String,
    val description: String?,
    val count: Int
)
