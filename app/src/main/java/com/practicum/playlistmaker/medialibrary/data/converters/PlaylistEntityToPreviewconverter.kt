package com.practicum.playlistmaker.medialibrary.data.converters

import android.net.Uri
import androidx.core.net.toUri
import com.practicum.playlistmaker.medialibrary.data.db.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview

fun PlaylistEntity.toPreview(uri: Uri?): PlaylistPreview {
    return with(this) {
        PlaylistPreview(
            id = id!!,
            iconUri = uri,
            name = name,
            count = chooseCase(count)
        )
    }
}

fun chooseCase(count: Int): String {
    var str =
        if ((count % 100) in (11..14)) "треков"
        else when (count % 10) {
            1 -> "трек"
            0, 5, 6, 7, 8, 9 -> "треков"
            else -> "трека"
        }
    return "$count $str"
}