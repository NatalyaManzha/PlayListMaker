package com.practicum.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInfoConsumer
import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerFeedbackData
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import java.util.Locale

class PlayerViewModel(val playerUiUpdater: PlayerUiUpdater) {
    private val likeChecker by lazy { Creator.provideCheckFavoriteTracksUseCase() }
    private val favorites by lazy { Creator.provideChangeFavoriteTracksUseCase() }
    private val mediaPlayerControler by lazy { Creator.provideControlMediaPlayerUseCase() }
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var runnable = Runnable { updatePlayerData() }
    private val updaterUi = object : MediaPlayerInfoConsumer {
        override fun consume(info: MediaPlayerFeedbackData) {
            when (info) {
                is MediaPlayerFeedbackData.State -> onPlayerStateChange(info.state)
                is MediaPlayerFeedbackData.CurrentPosition -> playerUiUpdater.onCurrentPositionChange(
                    dateFormat.format(info.currentPosition)
                )
            }
        }
    }

    private var isInFavorites = false
    fun onPlayerActivityDestroy() {
        sendCommandToMediaPlayer(MediaPlayerCommand.RELEASE)
    }

    fun onPlayerActivityPause() {
        sendCommandToMediaPlayer(MediaPlayerCommand.PAUSE)
        handler.removeCallbacksAndMessages(0)
    }

    private fun sendCommandToMediaPlayer(command: MediaPlayerCommand) {
        mediaPlayerControler.execute(MediaPlayerControllerCommand(command, null), updaterUi)
    }

    private fun onPlayerStateChange(state: MediaPlayerState) {
        with(playerUiUpdater) {
            when (state) {
                MediaPlayerState.PREPARED -> onPlayerStatePrepared()
                MediaPlayerState.PLAYBACK_COMPLETE -> onPlayerStatePlaybackComplete()
                MediaPlayerState.PLAYING -> onPlayerStatePlaying()
                MediaPlayerState.PAUSED -> onPlayerStatePaused()
                else -> {}
            }
        }
    }

    fun prepareMediaPlayer(previewUrl: String) {
        mediaPlayerControler.execute(
            MediaPlayerControllerCommand(
                MediaPlayerCommand.PREPARE,
                previewUrl
            ), updaterUi
        )
    }

    private fun updatePlayerData() {
        sendCommandToMediaPlayer(MediaPlayerCommand.GET_CURRENT_POSITION)
        sendCommandToMediaPlayer(MediaPlayerCommand.GET_STATE)
        if (playerUiUpdater.getPlayerActivityUiState()) handler.postDelayed(runnable, DELAY_MILLIS)
    }

    fun playbackControl(uiStateOnPlaying: Boolean) {
        if (uiStateOnPlaying) {
            sendCommandToMediaPlayer(MediaPlayerCommand.PAUSE)
        } else {
            sendCommandToMediaPlayer(MediaPlayerCommand.START)
            handler.post(runnable)
        }
    }

    fun checkFavorites(trackId: Int) {
        val isInFavorites = likeChecker.isInFavorites(trackId.toString())
        playerUiUpdater.setResourseToFavoritesButton(isInFavorites)
        this.isInFavorites = isInFavorites
    }

    fun toggleFavorite(trackId: Int) {
        val id = trackId.toString()
        if (isInFavorites) {
            favorites.removeFromFavorites(id)
            isInFavorites = false
            playerUiUpdater.setResourseToFavoritesButton(isInFavorites)
        } else {
            favorites.addToFavorites(id)
            isInFavorites = true
            playerUiUpdater.setResourseToFavoritesButton(isInFavorites)
        }
    }


    companion object {
        private const val DELAY_MILLIS = 300L
    }
}