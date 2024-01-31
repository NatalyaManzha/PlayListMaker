package com.practicum.playlistmaker.presentation.presenter

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.domain.api.MediaPlayerInfoConsumer
import com.practicum.playlistmaker.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData
import com.practicum.playlistmaker.domain.models.MediaPlayerState
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.api.PlayerUiUpdater
import java.util.Locale

class PlayerUiInteractor(val playerUiUpdater: PlayerUiUpdater) {
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

    fun onPlayerActivityDestroy() {
        sendCommandToMediaPlayer(MediaPlayerCommand.RELEASE)
    }

    fun onPlayerActivityPause() {
        sendCommandToMediaPlayer(MediaPlayerCommand.PAUSE)
        handler.removeCallbacksAndMessages(0)
    }

    fun prepareUiInteractor(track: Track) {
        prepareMediaPlayer(track)
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

    private fun prepareMediaPlayer(track: Track) {
        mediaPlayerControler.execute(
            MediaPlayerControllerCommand(
                MediaPlayerCommand.PREPARE,
                track.previewUrl
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

    companion object {
        private const val DELAY_MILLIS = 300L
    }
}