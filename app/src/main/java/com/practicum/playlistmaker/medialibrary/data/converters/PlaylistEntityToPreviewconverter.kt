package com.practicum.playlistmaker.medialibrary.data.converters

import com.practicum.playlistmaker.medialibrary.data.db.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview

fun PlaylistEntity.toPreview(): PlaylistPreview {
    return with(this) {
        PlaylistPreview(
            id = id!!,
            iconUrl = iconUrl,
            name = name,
            count = chooseCase(count)
        )
    }
}

fun List<PlaylistEntity>.converted(): List<PlaylistPreview> {
    return this.map{ playlistEntity ->
        with(playlistEntity) {
            PlaylistPreview(
                id = id!!,
                iconUrl = iconUrl,
                name = name,
                count = chooseCase(count)
            )
        }
    }
}

fun chooseCase(count: Int): String{
    var str =
    if ((count % 100) in (11..14)) "треков"
    else when (count % 10){
        1 -> "трек"
        0,5,6,7,8,9 -> "треков"
        else -> "трека"
    }
    return "$count $str"
}