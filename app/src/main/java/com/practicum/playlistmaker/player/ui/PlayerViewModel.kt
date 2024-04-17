package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.models.PlayerUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {


    private var uiStateOnPlaying = false
    private var isInFavoritesLiveData = MutableLiveData<Boolean>()
    private var playerStateLiveData = MutableLiveData(MediaPlayerState.DEFAULT)
    private var currentPositionLiveData = MutableLiveData(PLAYER_START_TIME)
    private var playerToBeResumed = false
    private var updateProgress: Job? = null
    private var track: Track? = null


    fun observeFavorites(): LiveData<Boolean> = isInFavoritesLiveData
    fun observePlayerState(): LiveData<MediaPlayerState> = playerStateLiveData
    fun observeCurrentPosition(): LiveData<String> = currentPositionLiveData

    fun onUiEvent(event: PlayerUiEvent) {
        when (event) {
            is PlayerUiEvent.OnViewCreated -> setTrackToPlay(event)
            is PlayerUiEvent.PlayControlButtonClick -> playbackControl()
            is PlayerUiEvent.AddToFavoritesButtonClick -> toggleFavorite()
            is PlayerUiEvent.OnResume -> onPlayerFragmentOnResume()
            is PlayerUiEvent.OnPause -> onPlayerFragmentPause()
        }
    }

    private fun setTrackToPlay(event: PlayerUiEvent.OnViewCreated) {
        if (track == null) {
            isInFavoritesLiveData.value = event.track.inFavorite
            track = event.track
            preparePlayer(event.track.previewUrl)
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayerInteractor.execute(
            MediaPlayerControllerCommand(
                MediaPlayerCommand.PREPARE,
                url
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

    private fun onPlayerFragmentOnResume() {
        if ((currentPositionLiveData.value != PLAYER_START_TIME) && playerToBeResumed)
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

    private fun toggleFavorite() {
        viewModelScope.launch {
            val isInFavorites = isInFavoritesLiveData.value ?: false
            with(favoritesInteractor) {
                if (isInFavorites) deleteFavorite(track!!.trackId)
                else insertFavorite(track!!)
            }
            isInFavoritesLiveData.postValue(!isInFavorites)
        }
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