package com.practicum.playlistmaker.medialibrary.ui.models

import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview

sealed interface PlaylistsUiState {
    object Default : PlaylistsUiState
    object Placeholder : PlaylistsUiState
    data class ShowPlaylists(val playlists: List<PlaylistPreview>) : PlaylistsUiState
}