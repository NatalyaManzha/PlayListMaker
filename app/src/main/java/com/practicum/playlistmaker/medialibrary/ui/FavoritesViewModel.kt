package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.ui.models.FavoritesUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<FavoritesUiState>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.getFavoritesFlow().collect { trackList ->
                stateLiveData.postValue(
                    if (trackList.isEmpty()) FavoritesUiState.Default
                    else FavoritesUiState.ShowFavorites(trackList)
                )
            }
        }
    }

    fun observeState(): LiveData<FavoritesUiState> = stateLiveData
}