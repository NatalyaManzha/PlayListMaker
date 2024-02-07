package com.practicum.playlistmaker.player.ui

interface PlayerUiUpdater {
    fun onPlayerStatePrepared()
    fun onPlayerStatePlaybackComplete()
    fun onPlayerStatePlaying()
    fun onPlayerStatePaused()
    fun onCurrentPositionChange(currentPosition: String)
    fun getPlayerActivityUiState(): Boolean
    fun setResourseToFavoritesButton(isInFavorites: Boolean)
}