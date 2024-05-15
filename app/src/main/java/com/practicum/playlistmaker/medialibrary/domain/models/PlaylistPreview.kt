package com.practicum.playlistmaker.medialibrary.domain.models

import android.net.Uri

data class PlaylistPreview(
    val id: Long,
    val iconUri: Uri?,
    val name: String,
    val count: String
)
