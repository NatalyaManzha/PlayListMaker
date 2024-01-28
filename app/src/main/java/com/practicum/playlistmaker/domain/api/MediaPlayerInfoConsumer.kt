package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData

interface MediaPlayerInfoConsumer {
    fun consume(info: MediaPlayerFeedbackData)
}