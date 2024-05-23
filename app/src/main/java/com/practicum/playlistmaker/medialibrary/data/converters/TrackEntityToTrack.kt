package com.practicum.playlistmaker.medialibrary.data.converters

import com.practicum.playlistmaker.medialibrary.data.db.favorites.TrackEntity
import com.practicum.playlistmaker.player.domain.models.Track

fun List<TrackEntity>.converted(): List<Track> {
    return this.asReversed()
        .map { trackEntity ->
            with(trackEntity) {
                Track(
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
                    trackTimeMinSec = trackTimeMinSec,
                    inFavorite = true
                )
            }
        }
}

