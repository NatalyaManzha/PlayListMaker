package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.ControlMediaPlayerUseCase
import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerFeedbackData

class ControlMediaPlayerUseCaseImpl(
    private val mediaPlayerController: MediaPlayerController
) : ControlMediaPlayerUseCase {
    override fun execute(
        controllerCommand: MediaPlayerControllerCommand,
        consumer: MediaPlayerController.Consumer
    ) {
        val info: MediaPlayerFeedbackData
        with(mediaPlayerController) {
            info = when (controllerCommand.command) {
                MediaPlayerCommand.PREPARE -> prepare(controllerCommand.url!!)
                MediaPlayerCommand.START -> start()
                MediaPlayerCommand.PAUSE -> pause()
                MediaPlayerCommand.RELEASE -> release()
                MediaPlayerCommand.GET_STATE -> getMediaPlayerState()
                MediaPlayerCommand.GET_CURRENT_POSITION -> getCurrentPosition()
            }
        }
        consumer.consume(info)
    }
}