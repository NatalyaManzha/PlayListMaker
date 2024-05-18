package com.practicum.playlistmaker.medialibrary.ui.models

import android.net.Uri
import com.practicum.playlistmaker.player.domain.models.Track

data class PlaylistFIUiState(
    val iconUri: Uri?,
    val name: String,
    val description: String,
    val count: String,
    val duration: String,
    val tracklist: List<Track>?
)
