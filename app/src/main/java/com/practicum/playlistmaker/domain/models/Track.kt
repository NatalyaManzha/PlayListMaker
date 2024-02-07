package com.practicum.playlistmaker.domain.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val artistName: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val collectionName: String,
    val country: String,
    val previewUrl: String,
    val primaryGenreName: String,
    val releaseYear: String,
    val trackId: Int,
    val trackName: String,
    val trackTimeMinSec: String
) : Serializable