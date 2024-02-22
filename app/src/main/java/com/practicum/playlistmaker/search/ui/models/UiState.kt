package com.practicum.playlistmaker.search.ui.models

import com.practicum.playlistmaker.player.domain.models.Track

sealed interface UiState {
    object Default : UiState
    data class SearchHistory(val tracklist: List<Track>) : UiState
    object ClearSearchHistory : UiState
    object Loading : UiState
    data class SearchResult(val tracklist: List<Track>) : UiState
    object EmptyResult : UiState
    object Error : UiState
}