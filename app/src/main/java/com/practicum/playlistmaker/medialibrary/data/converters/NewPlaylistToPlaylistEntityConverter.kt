package com.practicum.playlistmaker.medialibrary.data.converters

import com.practicum.playlistmaker.medialibrary.data.db.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist

const val COUNT_DEFAULT_VALUE_INT = 0

fun NewPlaylist.toPlaylistEntity(iconFileName: String): PlaylistEntity {
    return with(this) {
        PlaylistEntity(
            id = null,
            iconFileName = iconFileName,
            name = name,
            description = description,
            count = COUNT_DEFAULT_VALUE_INT
        )
    }
}