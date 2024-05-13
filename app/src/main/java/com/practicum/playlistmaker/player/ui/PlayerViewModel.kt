package com.practicum.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistsUiState
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerControllerCommand
import com.practicum.playlistmaker.player.domain.models.MediaPlayerState
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.player.ui.models.PlayerUiEvent
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

    private val _isInFavorites = MutableStateFlow(false)
    val isInFavoritesFlow = _isInFavorites.asStateFlow()
    private val _currentPosition = MutableStateFlow(PLAYER_START_TIME)
    val currentPosition = _currentPosition.asStateFlow()
    private val _playerState = MutableStateFlow(MediaPlayerState.DEFAULT)
    val playerStateFlow = _playerState.asStateFlow()
    private val _playlists = MutableStateFlow<List<PlaylistPreview>>(emptyList())
    val playlists = _playlists.asStateFlow()
    private val _saveTrackSuccess = MutableStateFlow<Pair<Boolean, String>?>(null)
    val saveTrackSuccess = _saveTrackSuccess.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylistPreviewFlow().collect { playlists ->
                _playlists.value = playlists
            }
        }
    }

    private var uiStateOnPlaying = false
    private var playerToBeResumed = false
    private var updateProgress: Job? = null
    private var track: Track? = null

    fun onUiEvent(event: PlayerUiEvent) {
        when (event) {
            is PlayerUiEvent.OnViewCreated -> setTrackToPlay(event)
            is PlayerUiEvent.PlayControlButtonClick -> playbackControl()
            is PlayerUiEvent.AddToFavoritesButtonClick -> toggleFavorite()
            is PlayerUiEvent.OnResume -> onPlayerFragmentOnResume()
            is PlayerUiEvent.OnPause -> onPlayerFragmentPause()
            is PlayerUiEvent.AddTrackToPlaylist -> addTrackToPlaylist(event.playlistID, event.playlistName)
        }
    }

    private fun addTrackToPlaylist(playlistID: Long, playlistName:String){
        viewModelScope.launch(Dispatchers.IO) {
            _saveTrackSuccess.value = Pair(
                playlistsInteractor.addTrackToPlaylist(playlistID, track!!),
                playlistName
            )
        }
    }

    private fun setTrackToPlay(event: PlayerUiEvent.OnViewCreated) {
        if (track == null) {
            _isInFavorites.value = event.track.inFavorite
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
                    _currentPosition.value = FULL_TIME
                    updateProgress?.cancel()
                    delay(DELAY_MILLIS)
                    uiStateOnPlaying = false
                    _currentPosition.value = PLAYER_START_TIME
                }
                _playerState.value = state
            }
        }
    }

    private fun onPlayerFragmentOnResume() {
        if ((_currentPosition.value != PLAYER_START_TIME) && playerToBeResumed)
            startPlayer()
        playerToBeResumed = false
    }

    private fun onPlayerFragmentPause() {
        if (_playerState.value == MediaPlayerState.PLAYING) playerToBeResumed = true
        pausePlayer()
    }

    private fun sendCommandToMediaPlayer(command: MediaPlayerCommand) {
        mediaPlayerInteractor.execute(MediaPlayerControllerCommand(command, null))
    }

    private fun updatePlayerData() {
        updateProgress = viewModelScope.launch(Dispatchers.IO) {
            mediaPlayerInteractor.playerCurrentPositionFlow().collect { time ->
                _currentPosition.value = time
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
            val isInFavorites = _isInFavorites.value
            with(favoritesInteractor) {
                try {
                    if (isInFavorites) deleteFavorite(track!!.trackId)
                    else insertFavorite(track!!)
                } catch (e: Exception) {
                    Log.e("BD", "Ошибка выполнения запроса в БД ${e.message}")
                }
            }
            _isInFavorites.value = !isInFavorites
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