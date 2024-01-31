package com.practicum.playlistmaker.domain.api.useCase

import com.practicum.playlistmaker.domain.api.MediaPlayerInfoConsumer
import com.practicum.playlistmaker.domain.models.MediaPlayerControllerCommand

interface ControlMediaPlayerUseCase {
    fun execute(
        controllerCommand: MediaPlayerControllerCommand,
        consumer: MediaPlayerInfoConsumer
    )
}