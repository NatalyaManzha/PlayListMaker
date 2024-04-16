package com.practicum.playlistmaker.player.ui.models

import com.practicum.playlistmaker.player.domain.models.Track

sealed interface PlayerUiEvent {
    data class OnViewCreated(val track: Track) : PlayerUiEvent
    object PlayControlButtonClick : PlayerUiEvent
    object AddToFavoritesButtonClick : PlayerUiEvent
    object OnResume : PlayerUiEvent
    object OnPause : PlayerUiEvent
}