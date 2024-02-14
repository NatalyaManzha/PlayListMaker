package com.practicum.playlistmaker.search.data.network.converters

import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.data.network.dto.TrackDto
import java.text.SimpleDateFormat
import java.util.Locale

object TrackDtoToTrackConverter {
    private val NUMBER_OF_CHARS_IN_YEAR = 4
    private val DEFAULT_STRING_VALUE = ""
    private val DEFAULT_INT_VALUE = 0
    fun convert(item: TrackDto): Track {
        return with(item) {
            Track(
                artistName = artistName ?: DEFAULT_STRING_VALUE,
                artworkUrl100 = artworkUrl100 ?: DEFAULT_STRING_VALUE,
                artworkUrl512 = getCoverArtwork(artworkUrl100),
                collectionName = collectionName ?: DEFAULT_STRING_VALUE,
                country = country ?: DEFAULT_STRING_VALUE,
                previewUrl = previewUrl ?: DEFAULT_STRING_VALUE,
                primaryGenreName = primaryGenreName ?: DEFAULT_STRING_VALUE,
                releaseYear = getReleaseYear(releaseDate),
                trackId = trackId ?: DEFAULT_INT_VALUE,
                trackName = trackName ?: DEFAULT_STRING_VALUE,
                trackTimeMinSec = getFormatedTime(trackTimeMillis),
                inFavorite = false
            )
        }
    }

    private fun getFormatedTime(trackTimeMillis: Long?): String {
        val time = trackTimeMillis ?: 0L
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    private fun getCoverArtwork(artworkUrl100: String?): String {
        return if (artworkUrl100 == null) DEFAULT_STRING_VALUE
        else artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }


    private fun getReleaseYear(releaseDate: String?): String {
        return if ((releaseDate == null) || (releaseDate.length < NUMBER_OF_CHARS_IN_YEAR)) DEFAULT_STRING_VALUE
        else releaseDate.substring(0, NUMBER_OF_CHARS_IN_YEAR)
    }
}