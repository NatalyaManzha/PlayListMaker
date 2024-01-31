package com.practicum.playlistmaker.data.network.converters

import com.practicum.playlistmaker.data.network.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDtoToTrackConverter {
    fun convert(item: TrackDto): Track {
        return with(item) {
            Track(
                artistName = artistName,
                artworkUrl100 = artworkUrl100,
                artworkUrl512 = getCoverArtwork(artworkUrl100),
                collectionName = collectionName,
                country = country,
                previewUrl = previewUrl,
                primaryGenreName = primaryGenreName,
                releaseYear = getReleaseYear(releaseDate),
                trackId = trackId,
                trackName = trackName,
                trackTimeMinSec = getFormatedTime(trackTimeMillis)
            )
        }
    }

    private fun getFormatedTime(trackTimeMillis: Long) =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    private fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private fun getReleaseYear(releaseDate: String): String {
        return if (releaseDate.length < NUMBER_OF_CHARS_IN_YEAR) ""
        else releaseDate.substring(0, NUMBER_OF_CHARS_IN_YEAR)
    }

    companion object {
        private const val NUMBER_OF_CHARS_IN_YEAR = 4
    }
}