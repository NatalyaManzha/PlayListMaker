package com.practicum.playlistmaker.medialibrary.data.converters

import com.practicum.playlistmaker.medialibrary.data.db.TrackEntity
import com.practicum.playlistmaker.player.domain.models.Track

object TrackToTrackEntityConverter {
    fun convert(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
                id = null,
                trackId = trackId,
                artistName = artistName,
                artworkUrl100 = artworkUrl100,
                artworkUrl512 = artworkUrl512,
                collectionName = collectionName,
                country = country,
                previewUrl = previewUrl,
                primaryGenreName = primaryGenreName,
                releaseYear = releaseYear,
                trackName = trackName,
                trackTimeMinSec = trackTimeMinSec
            )
        }
    }
}