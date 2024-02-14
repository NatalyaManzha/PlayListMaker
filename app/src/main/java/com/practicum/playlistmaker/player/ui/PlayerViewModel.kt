package com.practicum.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.utils.Creator
import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerFeedbackData
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState

class PlayerViewModel: ViewModel() {

    private val favoritesChecker by lazy { Creator.provideCheckFavoriteTracksUseCase() }
    private val favorites by lazy { Creator.provideChangeFavoriteTracksUseCase() }
    private val mediaPlayerController by lazy { Creator.provideControlMediaPlayerUseCase() }
    private val handler = Handler(Looper.getMainLooper())
    private var runnable = Runnable { updatePlayerData() }
    private var uiStateOnPlaying = false
    private var isInFavoritesLiveData = MutableLiveData<Boolean>()
    private var playerStateLiveData = MutableLiveData<MediaPlayerState>()
    private var currentPositionLiveData = MutableLiveData<String>()
    private var trackPreviewUrl = ""
    private var playerToBeResumed = false

    private val infoConsumer = object : MediaPlayerController.Consumer {
        override fun consume(info: MediaPlayerFeedbackData) {
            when (info) {
                is MediaPlayerFeedbackData.State -> {
                    playerStateLiveData.value = info.state
                    uiStateOnPlaying = (info.state == MediaPlayerState.PLAYING)
                    when (info.state) {
                        MediaPlayerState.DEFAULT,
                        MediaPlayerState.PREPARED,
                        MediaPlayerState.PLAYBACK_COMPLETE -> currentPositionLiveData.value =
                            PLAYER_START_TIME
                        else -> {}
                    }
                }
                is MediaPlayerFeedbackData.CurrentPosition -> currentPositionLiveData.value =
                    info.currentPosition
            }
        }
    }

    fun observeFavorites(): LiveData<Boolean> = isInFavoritesLiveData
    fun observePlayerState(): LiveData<MediaPlayerState> = playerStateLiveData
    fun observeCurrentPosition(): LiveData<String> = currentPositionLiveData
    fun preparePlayer(url: String) {
        if (trackPreviewUrl != url) {
            trackPreviewUrl = url
            prepareMediaPlayer("url")
        }
    }

    fun onPlayerActivityOnResume() {
        if ((currentPositionLiveData.value != PLAYER_START_TIME) && (playerToBeResumed == true))
            sendCommandToMediaPlayer(MediaPlayerCommand.START)
        handler.post(runnable)
        playerToBeResumed = false
    }

    fun onPlayerActivityPause() {
        if (playerStateLiveData.value == MediaPlayerState.PLAYING) playerToBeResumed = true
        sendCommandToMediaPlayer(MediaPlayerCommand.PAUSE)
        handler.removeCallbacksAndMessages(PLAYER_DATA_UPDATE_TOKEN)
    }

    private fun sendCommandToMediaPlayer(command: MediaPlayerCommand) {
        mediaPlayerController.execute(MediaPlayerControllerCommand(command, null), infoConsumer)
    }

    private fun prepareMediaPlayer(previewUrl: String) {
        infoConsumer.consume(MediaPlayerFeedbackData.State(MediaPlayerState.DEFAULT))
        mediaPlayerController.execute(
            MediaPlayerControllerCommand(
                MediaPlayerCommand.PREPARE,
                previewUrl
            ), infoConsumer
        )
    }

    private fun updatePlayerData() {
        sendCommandToMediaPlayer(MediaPlayerCommand.GET_CURRENT_POSITION)
        sendCommandToMediaPlayer(MediaPlayerCommand.GET_STATE)
        if (uiStateOnPlaying) {
            handler.postDelayed(
                runnable,
                PLAYER_DATA_UPDATE_TOKEN,
                DELAY_MILLIS
            )
        }
    }

    fun playbackControl() {
        if (uiStateOnPlaying) {
            sendCommandToMediaPlayer(MediaPlayerCommand.PAUSE)
        } else {
            sendCommandToMediaPlayer(MediaPlayerCommand.START)
            handler.post(runnable)
        }
    }

    fun checkFavorites(trackId: Int) {
        isInFavoritesLiveData.value = favoritesChecker.isInFavorites(trackId.toString())
    }

    fun toggleFavorite(trackId: Int) {
        val id = trackId.toString()
        val isInFavorites = isInFavoritesLiveData.value ?: false
        if (isInFavorites) favorites.removeFromFavorites(id)
        else favorites.addToFavorites(id)
        this.isInFavoritesLiveData.value = !isInFavorites
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(PLAYER_DATA_UPDATE_TOKEN)
        sendCommandToMediaPlayer(MediaPlayerCommand.RELEASE)
    }


    companion object {
        private const val DELAY_MILLIS = 250L
        private const val PLAYER_START_TIME = "00:00"
        private val PLAYER_DATA_UPDATE_TOKEN = Any()
    }
}