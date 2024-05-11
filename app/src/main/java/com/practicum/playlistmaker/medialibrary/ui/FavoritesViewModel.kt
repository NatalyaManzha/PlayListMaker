package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.ui.models.FavoritesUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Default)
    val uiStateFlow = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.getFavoritesFlow().collect { trackList ->
                _uiState.value =
                    if (trackList.isEmpty()) FavoritesUiState.Placeholder
                    else FavoritesUiState.ShowFavorites(trackList)
            }
        }
    }
}