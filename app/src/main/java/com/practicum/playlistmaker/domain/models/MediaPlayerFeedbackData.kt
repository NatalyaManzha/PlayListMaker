package com.practicum.playlistmaker.domain.models

sealed interface MediaPlayerFeedbackData {
    data class State(val state: MediaPlayerState) : MediaPlayerFeedbackData
    data class CurrentPosition(val currentPosition: Int) : MediaPlayerFeedbackData
}