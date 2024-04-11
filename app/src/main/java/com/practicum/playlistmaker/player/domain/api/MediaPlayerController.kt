package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import kotlinx.coroutines.flow.Flow

interface MediaPlayerController {
    fun updateState(): Flow<MediaPlayerState>
    fun updateProgress(): Flow<String>
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
}