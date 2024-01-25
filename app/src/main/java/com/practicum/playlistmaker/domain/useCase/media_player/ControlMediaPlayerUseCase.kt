package com.practicum.playlistmaker.domain.useCase.media_player

import com.practicum.playlistmaker.domain.COMMAND_GET_CURRENT_POSITION
import com.practicum.playlistmaker.domain.COMMAND_GET_STATE
import com.practicum.playlistmaker.domain.COMMAND_PAUSE
import com.practicum.playlistmaker.domain.COMMAND_PREPARE
import com.practicum.playlistmaker.domain.COMMAND_RELEASE
import com.practicum.playlistmaker.domain.COMMAND_START
import com.practicum.playlistmaker.domain.STATE_DEFAULT
import com.practicum.playlistmaker.domain.api.MediaPlayerControl
import com.practicum.playlistmaker.domain.api.MediaPlayerInfoConsumer
import com.practicum.playlistmaker.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData

class ControlMediaPlayerUseCase(private val mediaPlayerControl: MediaPlayerControl) {
    fun execute(
        controllerCommand: MediaPlayerControllerCommand,
        consumer: MediaPlayerInfoConsumer
    ) {
        val info: MediaPlayerFeedbackData
        with(mediaPlayerControl) {
            info = when (controllerCommand.command) {
                COMMAND_PREPARE -> prepare(controllerCommand.url!!)
                COMMAND_START -> start()
                COMMAND_PAUSE -> pause()
                COMMAND_RELEASE -> release()
                COMMAND_GET_STATE -> getMediaPlayerState()
                COMMAND_GET_CURRENT_POSITION -> getCurrentPosition()
                else -> MediaPlayerFeedbackData.State(STATE_DEFAULT)
            }
        }
        consumer.consume(info)
    }
}