package com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistFIUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistFIUiState
import com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo.utils.countDuration
import com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo.utils.formMessage
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.sharing.domain.api.SharePlaylistUseCase
import com.practicum.playlistmaker.utils.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistFIViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val sharePlaylistUseCase: SharePlaylistUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private var playlistID: Long? = null
    private var playlistInfoJob: Job? = null
    private var tracklistInfoJob: Job? = null
    private val _uiState = MutableStateFlow(
        PlaylistFIUiState(
            iconUri = null,
            name = STRING_DEFAULT_VALUE,
            description = STRING_DEFAULT_VALUE,
            count = STRING_DEFAULT_VALUE,
            duration = STRING_DEFAULT_VALUE,
            tracklist = null,
            isInFavorites = null,
            playlistDeleted = null
        )
    )
    val uiState = _uiState.asStateFlow()

    fun onUiEvent(event: PlaylistFIUiEvent) {
        when (event) {
            is PlaylistFIUiEvent.OnCreateView -> subscribeOnData(event.playlistID)
            is PlaylistFIUiEvent.OnTrackClick -> checkInFavorites(event.trackID)
            is PlaylistFIUiEvent.DeleteTrack -> deleteTrack(event.trackID)
            PlaylistFIUiEvent.SharePlaylist -> sharePlaylist()
            PlaylistFIUiEvent.DeletePlaylist -> deletePlaylist()
        }
    }

    private fun sharePlaylist() {
        val message =
           with (_uiState.value){
                formMessage(
                    playlistInfo = listOf(name, description, count),
                    tracklist = tracklist!!
                )
            }
        sharePlaylistUseCase.execute(message)
    }

    private fun deletePlaylist() {
        playlistInfoJob?.cancel()
        tracklistInfoJob?.cancel()
        viewModelScope.launch(Dispatchers.IO) {
            val result = playlistsInteractor.deletePlaylist(playlistID!!)
            _uiState.value = _uiState.value.copy(playlistDeleted = result)
            if (!result) {
                delay(COPY_STATE_DELAY_MILLIS)
                _uiState.value = _uiState.value.copy(playlistDeleted = null)
            }
        }
    }

    private fun deleteTrack(trackID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.deleteTrackFromPlaylist(playlistID!!, trackID)
        }
    }

    private fun checkInFavorites(trackID: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isInFavorites = favoritesInteractor.checkTrackInFavorites(trackID)
            )
            delay(COPY_STATE_DELAY_MILLIS)
            _uiState.value = _uiState.value.copy(isInFavorites = null)
        }
    }

    private fun subscribeOnData(id: Long) {
        if (playlistID == null) {
            playlistID = id
            playlistInfoJob = viewModelScope.launch(Dispatchers.IO) {
                playlistsInteractor.getPlaylistInfoFlow(id).collect { playlistInfo ->
                    with(playlistInfo) {
                        _uiState.value = _uiState.value.copy(
                            iconUri = iconUri,
                            name = name,
                            description = description,
                            count = count
                        )
                    }
                }
            }
            tracklistInfoJob = viewModelScope.launch(Dispatchers.IO) {
                playlistsInteractor.getTrackIdListFlow(id).collect { idList ->
                    if (idList.isEmpty()) _uiState.value = _uiState.value.copy(
                        tracklist = null,
                        duration = resourceProvider.getString(R.string.zero_minutes)
                    )
                    else {
                        val trackList = mutableListOf<Track>()
                        idList.reversed().forEach { trackID ->
                            trackList.add(playlistsInteractor.getTrackByID(trackID))
                        }
                        _uiState.value = _uiState.value.copy(
                            tracklist = trackList,
                            duration = countDuration(trackList)
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val STRING_DEFAULT_VALUE = ""
        private const val COPY_STATE_DELAY_MILLIS = 1L
    }
}