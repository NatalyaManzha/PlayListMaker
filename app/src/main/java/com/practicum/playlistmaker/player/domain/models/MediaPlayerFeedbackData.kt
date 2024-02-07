package com.practicum.playlistmaker.player.domain.models

sealed interface MediaPlayerFeedbackData {
    data class State(val state: MediaPlayerState) : MediaPlayerFeedbackData
    data class CurrentPosition(val currentPosition: Int) : MediaPlayerFeedbackData
}