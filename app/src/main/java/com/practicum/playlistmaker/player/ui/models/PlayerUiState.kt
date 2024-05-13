package com.practicum.playlistmaker.player.ui.models

import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState

data class PlayerUiState(
    val isInFavorites: Boolean,
    val currentPosition: String,
    val playerState: MediaPlayerState,
    val playlists: List<PlaylistPreview>,
    val saveTrackSuccess: Boolean?,
    val playlistName: String?
)
