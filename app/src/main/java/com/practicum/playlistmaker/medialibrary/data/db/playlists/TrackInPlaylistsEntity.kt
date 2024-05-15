package com.practicum.playlistmaker.medialibrary.data.db.playlists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackInPlaylistsEntity(
    @PrimaryKey
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
