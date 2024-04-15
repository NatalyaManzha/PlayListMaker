package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.api.ChangeFavoriteTracksUseCase
import com.practicum.playlistmaker.player.domain.api.CheckFavoriteTracksUseCase
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import com.practicum.playlistmaker.player.ui.models.PlayerUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val checkFavoriteTracksUseCase: CheckFavoriteTracksUseCase,
    private val changeFavoriteTracksUseCase: ChangeFavoriteTracksUseCase,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {


    private var uiStateOnPlaying = false
    private var isInFavoritesLiveData = MutableLiveData<Boolean>()
    private var playerStateLiveData = MutableLiveData<MediaPlayerState>(MediaPlayerState.DEFAULT)
    private var currentPositionLiveData = MutableLiveData<String>(PLAYER_START_TIME)
    private var trackPreviewUrl = ""
    private var playerToBeResumed = false
    private var updateProgress: Job? = null


    fun observeFavorites(): LiveData<Boolean> = isInFavoritesLiveData
    fun observePlayerState(): LiveData<MediaPlayerState> = playerStateLiveData
    fun observeCurrentPosition(): LiveData<String> = currentPositionLiveData

    fun onUiEvent(event: PlayerUiEvent) {
        when (event) {
            is PlayerUiEvent.OnViewCreated -> {
                checkFavorites(event.trackId)
                preparePlayer(event.previewUrl)
            }

            is PlayerUiEvent.PlayControlButtonClick -> playbackControl()
            is PlayerUiEvent.AddToFavoritesButtonClick -> toggleFavorite(event.trackId)
            is PlayerUiEvent.OnResume -> onPlayerFragmentOnResume()
            is PlayerUiEvent.OnPause -> onPlayerFragmentPause()
        }
    }

    private fun preparePlayer(url: String) {
        if (trackPreviewUrl != url) {
            trackPreviewUrl = url
            mediaPlayerInteractor.execute(
                MediaPlayerControllerCommand(
                    MediaPlayerCommand.PREPARE,
                    trackPreviewUrl
                )
            )

            viewModelScope.launch(Dispatchers.IO) {
                mediaPlayerInteractor.playerStateFlow().collect { state ->
                    if (state == MediaPlayerState.PLAYBACK_COMPLETE) {
                        currentPositionLiveData.postValue(FULL_TIME)
                        updateProgress?.cancel()
                        delay(DELAY_MILLIS)
                        uiStateOnPlaying = false
                        currentPositionLiveData.postValue(PLAYER_START_TIME)
                    }
                    playerStateLiveData.postValue(state)
                }
            }
        }
    }

    private fun onPlayerFragmentOnResume() {
        if ((currentPositionLiveData.value != PLAYER_START_TIME) && (playerToBeResumed == true))
            startPlayer()
        playerToBeResumed = false
    }

    private fun onPlayerFragmentPause() {
        if (playerStateLiveData.value == MediaPlayerState.PLAYING) playerToBeResumed = true
        pausePlayer()
    }

    private fun sendCommandToMediaPlayer(command: MediaPlayerCommand) {
        mediaPlayerInteractor.execute(MediaPlayerControllerCommand(command, null))
    }

    private fun updatePlayerData() {
        updateProgress = viewModelScope.launch(Dispatchers.IO) {
            mediaPlayerInteractor.playerCurrentPositionFlow().collect { time ->
                currentPositionLiveData.postValue(time)
            }
        }
    }

    private fun startPlayer() {
        uiStateOnPlaying = true
        sendCommandToMediaPlayer(MediaPlayerCommand.START)
        updatePlayerData()
    }

    private fun pausePlayer() {
        uiStateOnPlaying = false
        sendCommandToMediaPlayer(MediaPlayerCommand.PAUSE)
        updateProgress?.cancel()
    }

    private fun playbackControl() {
        if (uiStateOnPlaying) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    private fun checkFavorites(trackId: Int) {
        isInFavoritesLiveData.value = checkFavoriteTracksUseCase.isInFavorites(trackId.toString())
    }

    private fun toggleFavorite(trackId: Int) {
        val id = trackId.toString()
        val isInFavorites = isInFavoritesLiveData.value ?: false
        with(changeFavoriteTracksUseCase) {
            if (isInFavorites) removeFromFavorites(id)
            else addToFavorites(id)
        }
        this.isInFavoritesLiveData.value = !isInFavorites
    }

    override fun onCleared() {
        sendCommandToMediaPlayer(MediaPlayerCommand.RELEASE)
    }

    companion object {
        private const val PLAYER_START_TIME = "00:00"
        private const val FULL_TIME = "30:00"
        private const val DELAY_MILLIS = 300L
    }
}