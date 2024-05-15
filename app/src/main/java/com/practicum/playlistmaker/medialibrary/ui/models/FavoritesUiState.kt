package com.practicum.playlistmaker.medialibrary.ui.models

import com.practicum.playlistmaker.player.domain.models.Track

sealed interface FavoritesUiState {
    object Default : FavoritesUiState
    object Placeholder : FavoritesUiState
    data class ShowFavorites(val tracklist: List<Track>) : FavoritesUiState
}