package com.practicum.playlistmaker.domain.api

interface PlayerUiUpdater {
    fun onPlayerStatePrepared()
    fun onPlayerStatePlaybackComplete()
    fun onPlayerStatePlaying()
    fun onPlayerStatePaused()
    fun onCurrentPositionChange(currentPosition: Int)
    fun getPlayerActivityUiState():Boolean
}