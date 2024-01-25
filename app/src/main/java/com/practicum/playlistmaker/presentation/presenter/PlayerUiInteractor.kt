package com.practicum.playlistmaker.presentation.presenter

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.domain.COMMAND_GET_CURRENT_POSITION
import com.practicum.playlistmaker.domain.COMMAND_GET_STATE
import com.practicum.playlistmaker.domain.COMMAND_PAUSE
import com.practicum.playlistmaker.domain.COMMAND_PREPARE
import com.practicum.playlistmaker.domain.COMMAND_RELEASE
import com.practicum.playlistmaker.domain.COMMAND_START
import com.practicum.playlistmaker.domain.STATE_PAUSED
import com.practicum.playlistmaker.domain.STATE_PLAYBACK_COMPLETE
import com.practicum.playlistmaker.domain.STATE_PLAYING
import com.practicum.playlistmaker.domain.STATE_PREPARED
import com.practicum.playlistmaker.domain.api.MediaPlayerInfoConsumer
import com.practicum.playlistmaker.domain.api.PlayerUiUpdater
import com.practicum.playlistmaker.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.domain.models.MediaPlayerFeedbackData
import com.practicum.playlistmaker.domain.models.Track

class PlayerUiInteractor(val testPlayerUiUpdater: PlayerUiUpdater) {
    private val mediaPlayerControler by lazy { Creator.provideControlMediaPlayerUseCase() }
    private val handler = Handler(Looper.getMainLooper())
    private var runnable = Runnable { updatePlayerData() }
    val updaterUi = object : MediaPlayerInfoConsumer {
        override fun consume(info: MediaPlayerFeedbackData) {
            when (info) {
                is MediaPlayerFeedbackData.State -> onPlayerStateChange(info.state)
                is MediaPlayerFeedbackData.CurrentPosition -> testPlayerUiUpdater.onCurrentPositionChange(info.currentPosition)
            }
        }
    }

    fun onPlayerActivityDestroy(){
        sendCommandToMediaPlayer(COMMAND_RELEASE)
    }
    fun onPlayerActivityPause(){
        sendCommandToMediaPlayer(COMMAND_PAUSE)
        handler.removeCallbacksAndMessages(0)
    }
    fun prepareUiInteractor(track:Track) {
        prepareMediaPlayer(track)
    }

    private fun sendCommandToMediaPlayer(command: Int) {
        mediaPlayerControler.execute(MediaPlayerControllerCommand(command, null), updaterUi)
    }
    private fun onPlayerStateChange(state: Int) {
        with(testPlayerUiUpdater) {
            when (state) {
                STATE_PREPARED -> onPlayerStatePrepared()
                STATE_PLAYBACK_COMPLETE -> onPlayerStatePlaybackComplete()
                STATE_PLAYING -> onPlayerStatePlaying()
                STATE_PAUSED -> onPlayerStatePaused()
            }
        }
    }
    private fun prepareMediaPlayer(track:Track) {
        mediaPlayerControler.execute(
            MediaPlayerControllerCommand(
                COMMAND_PREPARE,
                track.previewUrl
            ), updaterUi
        )
    }
    private fun updatePlayerData() {
        sendCommandToMediaPlayer(COMMAND_GET_CURRENT_POSITION)
        sendCommandToMediaPlayer(COMMAND_GET_STATE)
        if (testPlayerUiUpdater.getPlayerActivityUiState()) handler.postDelayed(runnable, DELAY)
    }

    fun playbackControl(uiStateOnPlaying:Boolean) {
        if (uiStateOnPlaying) {
            sendCommandToMediaPlayer(COMMAND_PAUSE)
        } else {
            sendCommandToMediaPlayer(COMMAND_START)
            handler.post(runnable)
        }
    }

    companion object {
        private const val DELAY = 300L
    }

}