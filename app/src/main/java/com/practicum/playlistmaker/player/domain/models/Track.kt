package com.practicum.playlistmaker.player.domain.models

import java.io.Serializable

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
    val trackTimeMinSec: String,
    val inFavorite: Boolean
) : Serializable