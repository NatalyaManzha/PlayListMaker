package com.practicum.playlistmaker.domain.impl.useCase.media_player

import com.practicum.playlistmaker.domain.api.MediaPlayerControl
import com.practicum.playlistmaker.domain.api.MediaPlayerInfoConsumer
import com.practicum.playlistmaker.domain.api.useCase.ControlMediaPlayer
import com.practicum.playlistmaker.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData

class ControlMediaPlayerUseCase(
    private val mediaPlayerControl: MediaPlayerControl
) : ControlMediaPlayer {
    override fun execute(
        controllerCommand: MediaPlayerControllerCommand,
        consumer: MediaPlayerInfoConsumer
    ) {
        val info: MediaPlayerFeedbackData
        with(mediaPlayerControl) {
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