package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.medialibrary.ui.models.PlaylistsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaylistsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PlaylistsUiState>(PlaylistsUiState.Default)
    val uiStateFlow = _uiState.asStateFlow()

}