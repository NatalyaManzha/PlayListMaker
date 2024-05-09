package com.practicum.playlistmaker.medialibrary.ui.models

import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track

sealed interface PlaylistsUiState {
    object Default : PlaylistsUiState
    data class ShowPlaylists(val playlists: List<PlaylistPreview>) : PlaylistsUiState
}