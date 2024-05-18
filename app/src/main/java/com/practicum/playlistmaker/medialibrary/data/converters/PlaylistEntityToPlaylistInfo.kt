package com.practicum.playlistmaker.medialibrary.data.converters

import android.net.Uri
import com.practicum.playlistmaker.medialibrary.data.db.playlists.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistInfo

fun PlaylistEntity.toPlaylistInfo(uri: Uri?): PlaylistInfo {
    return with(this) {
        PlaylistInfo(
            iconUri = uri,
            name = name,
            description = description!!,
            count = chooseCase(count)
        )
    }
}

