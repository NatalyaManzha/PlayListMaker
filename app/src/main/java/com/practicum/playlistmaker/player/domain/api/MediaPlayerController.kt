package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.models.MediaPlayerFeedbackData

interface MediaPlayerController {
    fun getMediaPlayerState(): MediaPlayerFeedbackData.State
    fun getCurrentPosition(): MediaPlayerFeedbackData.CurrentPosition
    fun prepare(url: String): MediaPlayerFeedbackData.State
    fun start(): MediaPlayerFeedbackData.State
    fun pause(): MediaPlayerFeedbackData.State
    fun release(): MediaPlayerFeedbackData.State
}