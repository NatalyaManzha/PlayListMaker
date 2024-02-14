package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand

interface ControlMediaPlayerUseCase {
    fun execute(
        controllerCommand: MediaPlayerControllerCommand,
        consumer: MediaPlayerController.Consumer
    )

}