package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.models.MediaPlayerFeedbackData

interface MediaPlayerInfoConsumer {
    fun consume(info: MediaPlayerFeedbackData)
}