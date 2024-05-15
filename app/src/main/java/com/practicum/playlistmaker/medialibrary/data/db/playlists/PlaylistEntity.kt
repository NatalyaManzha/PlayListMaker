package com.practicum.playlistmaker.medialibrary.data.db.playlists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val iconFileName: String,
    val name: String,
    val description: String?,
    val count: Int
)
