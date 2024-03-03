package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.medialibrary.ui.models.FragmentUiState

class FavoritesViewModel : ViewModel() {
    private val stateLiveData = MutableLiveData<FragmentUiState>()

    init {
        stateLiveData.value = FragmentUiState.Default
    }

    fun observeState(): LiveData<FragmentUiState> = stateLiveData
}