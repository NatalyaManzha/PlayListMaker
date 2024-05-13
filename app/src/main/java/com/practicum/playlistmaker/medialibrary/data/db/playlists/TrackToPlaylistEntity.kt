package com.practicum.playlistmaker.medialibrary.data.db.playlists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track_table")
data class TrackToPlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val playlistId: Long,
    val trackId: Int
)
