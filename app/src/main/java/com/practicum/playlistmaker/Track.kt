package com.practicum.playlistmaker

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track  (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
): Serializable {

    fun getFormatedTime ()= SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}