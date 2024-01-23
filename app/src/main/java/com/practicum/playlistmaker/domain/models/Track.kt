package com.practicum.playlistmaker.domain.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val artistName: String,
    val artworkUrl100: String,
    val collectionName: String,
    val country: String,
    val previewUrl: String,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackId: Int,
    val trackName: String,
    val trackTimeMillis: Long
) : Serializable {

    fun getFormatedTime() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun getReleaseYear(): String {
        return if (releaseDate.length< NUMBER_OF_CHARS_IN_YEAR) ""
        else releaseDate.substring(0, NUMBER_OF_CHARS_IN_YEAR)
    }

    companion object{
        private const val NUMBER_OF_CHARS_IN_YEAR = 4
    }
}