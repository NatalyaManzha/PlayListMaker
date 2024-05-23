package com.practicum.playlistmaker.medialibrary.domain.models

import android.net.Uri

data class PlaylistInfo(
    val iconUri: Uri?,
    val name: String,
    val description: String,
    val count: String
)
