package com.practicum.playlistmaker.medialibrary.data.converters

import com.practicum.playlistmaker.medialibrary.data.db.playlists.TrackInPlaylistsEntity
import com.practicum.playlistmaker.player.domain.models.Track

fun TrackInPlaylistsEntity.toTrack(): Track {
    return with(this) {
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
                    inFavorite = false
                )
            }
}
