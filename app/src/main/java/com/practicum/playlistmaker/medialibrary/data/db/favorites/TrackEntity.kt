package com.practicum.playlistmaker.medialibrary.data.db.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val trackId: Int,
    val artistName: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val collectionName: String,
    val country: String,
    val previewUrl: String,
    val primaryGenreName: String,
    val releaseYear: String,
    val trackName: String,
    val trackTimeMinSec: String
)
