package com.practicum.playlistmaker.medialibrary.domain.models

import android.net.Uri
import java.io.Serializable

data class EditPlaylist(
    val id: Long,
    val iconUri: Uri?,
    val name: String,
    val description: String
) : Serializable