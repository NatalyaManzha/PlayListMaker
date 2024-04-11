package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import kotlinx.coroutines.flow.Flow

interface MediaPlayerInteractor {
    fun execute(controllerCommand: MediaPlayerControllerCommand)
    fun playerStateFlow(): Flow<MediaPlayerState>
    fun playerCurrentPositionFlow(): Flow<String>
}