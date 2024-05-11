package com.practicum.playlistmaker.medialibrary.domain.models

import android.net.Uri

data class NewPlaylist(
    val iconUri: Uri?,
    val name: String,
    val description: String?
)