package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlaylistsUiState>(PlaylistsUiState.Default)
    val uiStateFlow = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylistPreviewFlow().collect { playlists ->
                _uiState.value =
                    if (playlists.isEmpty()) PlaylistsUiState.Placeholder
                    else PlaylistsUiState.ShowPlaylists(playlists)
            }
        }
    }
}