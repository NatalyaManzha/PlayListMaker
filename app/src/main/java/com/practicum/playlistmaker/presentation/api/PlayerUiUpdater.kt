package com.practicum.playlistmaker.presentation.api

interface PlayerUiUpdater {
    fun onPlayerStatePrepared()
    fun onPlayerStatePlaybackComplete()
    fun onPlayerStatePlaying()
    fun onPlayerStatePaused()
    fun onCurrentPositionChange(currentPosition: String)
    fun getPlayerActivityUiState(): Boolean
}