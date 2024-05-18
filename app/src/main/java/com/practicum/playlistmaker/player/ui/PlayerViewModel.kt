package com.practicum.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.models.PlayerUiEvent
import com.practicum.playlistmaker.player.ui.models.PlayerUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private var uiStateOnPlaying = false
    private var playerToBeResumed = false
    private var updateProgress: Job? = null
    private var track: Track? = null
    private val _uiState = MutableStateFlow(
        PlayerUiState(
            isInFavorites = false,
            currentPosition = PLAYER_START_TIME,
            playerState = MediaPlayerState.DEFAULT,
            playlists = emptyList(),
            saveTrackSuccess = null,
            playlistName = null
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylistPreviewFlow().collect { playlists ->
                _uiState.value = _uiState.value.copy(playlists = playlists)
            }
        }
    }

    fun onUiEvent(event: PlayerUiEvent) {
        when (event) {
            is PlayerUiEvent.OnViewCreated -> setTrackToPlay(event)
            is PlayerUiEvent.PlayControlButtonClick -> playbackControl()
            is PlayerUiEvent.AddToFavoritesButtonClick -> toggleFavorite()
            is PlayerUiEvent.OnResume -> onPlayerFragmentOnResume()
            is PlayerUiEvent.OnPause -> onPlayerFragmentPause()
            is PlayerUiEvent.AddTrackToPlaylist -> addTrackToPlaylist(
                event.playlistID,
                event.playlistName
            )
        }
    }

    private fun addTrackToPlaylist(playlistID: Long, playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                saveTrackSuccess = playlistsInteractor.addTrackToPlaylist(playlistID, track!!),
                playlistName = playlistName
            )
            delay(COPY_STATE_DELAY_MILLIS)
            _uiState.value = _uiState.value.copy(
                saveTrackSuccess = null,
                playlistName = null
            )
        }
    }

    private fun setTrackToPlay(event: PlayerUiEvent.OnViewCreated) {
        if (track == null) {
            _uiState.value = _uiState.value.copy(
                isInFavorites = event.track.inFavorite
            )
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
                    _uiState.value = _uiState.value.copy(
                        currentPosition = FULL_TIME
                    )
                    updateProgress?.cancel()
                    delay(UPDATE_DELAY_MILLIS)
                    uiStateOnPlaying = false
                    _uiState.value = _uiState.value.copy(
                        currentPosition = PLAYER_START_TIME
                    )
                }
                _uiState.value = _uiState.value.copy(
                    playerState = state
                )
            }
        }
    }

    private fun onPlayerFragmentOnResume() {
        if ((_uiState.value.currentPosition != PLAYER_START_TIME) && playerToBeResumed)
            startPlayer()
        playerToBeResumed = false
    }

    private fun onPlayerFragmentPause() {
        if (_uiState.value.playerState == MediaPlayerState.PLAYING) playerToBeResumed = true
        pausePlayer()
    }

    private fun sendCommandToMediaPlayer(command: MediaPlayerCommand) {
        mediaPlayerInteractor.execute(MediaPlayerControllerCommand(command, null))
    }

    private fun updatePlayerData() {
        updateProgress = viewModelScope.launch(Dispatchers.IO) {
            mediaPlayerInteractor.playerCurrentPositionFlow().collect { time ->
                _uiState.value = _uiState.value.copy(
                    currentPosition = time
                )
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
        viewModelScope.launch(Dispatchers.IO) {
            val isInFavorites = _uiState.value.isInFavorites
            with(favoritesInteractor) {
                try {
                    if (isInFavorites) deleteFavorite(track!!.trackId)
                    else insertFavorite(track!!)
                } catch (e: Exception) {
                    Log.e("BD", "Ошибка выполнения запроса в БД ${e.message}")
                }
            }
            _uiState.value = _uiState.value.copy(
                isInFavorites = !isInFavorites
            )
        }
    }

    override fun onCleared() {
        sendCommandToMediaPlayer(MediaPlayerCommand.RELEASE)
    }

    companion object {
        private const val PLAYER_START_TIME = "00:00"
        private const val FULL_TIME = "30:00"
        private const val UPDATE_DELAY_MILLIS = 300L
        private const val COPY_STATE_DELAY_MILLIS = 1L
    }
}