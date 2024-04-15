package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import kotlinx.coroutines.flow.Flow

class MediaPlayerInteractorImpl(
    private val mediaPlayerController: MediaPlayerController
) : MediaPlayerInteractor {
    override fun execute(controllerCommand: MediaPlayerControllerCommand) {
        with(mediaPlayerController) {
            when (controllerCommand.command) {
                MediaPlayerCommand.PREPARE -> prepare(controllerCommand.url!!)
                MediaPlayerCommand.START -> start()
                MediaPlayerCommand.PAUSE -> pause()
                MediaPlayerCommand.RELEASE -> release()
            }
        }
    }

    override fun playerStateFlow(): Flow<MediaPlayerState> = mediaPlayerController.updateState()

    override fun playerCurrentPositionFlow(): Flow<String> = mediaPlayerController.updateProgress()

}