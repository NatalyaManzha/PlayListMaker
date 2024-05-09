package com.practicum.playlistmaker.medialibrary.data.converters

import com.practicum.playlistmaker.medialibrary.data.db.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.models.Playlist

fun Playlist.toPlaylistEntity(): PlaylistEntity {
    return with(this) {
        PlaylistEntity(
            id = null,
            iconUrl = iconUrl,
            name = name,
            description = description,
            count = count
        )
    }
}