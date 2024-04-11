package com.practicum.playlistmaker.player.ui.models

sealed interface PlayerUiEvent {
    data class OnViewCreated(val trackId: Int, val previewUrl: String) : PlayerUiEvent
    object PlayControlButtonClick : PlayerUiEvent
    data class AddToFavoritesButtonClick(val trackId: Int) : PlayerUiEvent
    object OnResume : PlayerUiEvent
    object OnPause : PlayerUiEvent
}