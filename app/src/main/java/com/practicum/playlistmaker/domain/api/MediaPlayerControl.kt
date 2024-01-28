package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData

interface MediaPlayerControl {
    fun getMediaPlayerState(): MediaPlayerFeedbackData.State
    fun getCurrentPosition(): MediaPlayerFeedbackData.CurrentPosition
    fun prepare(url: String): MediaPlayerFeedbackData.State
    fun start(): MediaPlayerFeedbackData.State
    fun pause(): MediaPlayerFeedbackData.State
    fun release(): MediaPlayerFeedbackData.State
}