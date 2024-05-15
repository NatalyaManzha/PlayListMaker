package com.practicum.playlistmaker.medialibrary.domain.models

data class Playlist(
    val id: Long?,
    val iconUrl: String,
    val name: String,
    val description: String,
    val count: Int
)
