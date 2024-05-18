package com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistFIUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistFIUiState
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.utils.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistFIViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private var playlistID: Long? = null
    private val _uiState = MutableStateFlow(
        PlaylistFIUiState(
            iconUri = null,
            name = STRING_DEFAULT_VALUE,
            description = STRING_DEFAULT_VALUE,
            count = STRING_DEFAULT_VALUE,
            duration = STRING_DEFAULT_VALUE,
            tracklist = null
        )
    )
    val uiState = _uiState.asStateFlow()
    fun onUiEvent(event: PlaylistFIUiEvent) {
        when (event) {
            is PlaylistFIUiEvent.OnCreateView -> subscribeOnData(event.playlistID)
        }
    }

    private fun subscribeOnData(id: Long) {
        if (playlistID == null) {
            playlistID = id
            viewModelScope.launch(Dispatchers.IO) {
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
            viewModelScope.launch(Dispatchers.IO) {
                playlistsInteractor.getTrackIdListFlow(id).collect { idList ->
                    if (idList.isEmpty()) _uiState.value = _uiState.value.copy(
                        tracklist = null,
                        duration = resourceProvider.getString(R.string.zero_minutes)
                    )
                    else {
                        val trackList = mutableListOf<Track>()
                        idList.forEach { trackID ->
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
    }
}