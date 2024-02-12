package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.player.domain.models.Track

interface SearchHistoryRepository {
    fun getSearchHistoryList(): List<Track>
    fun saveSearchHistory(searchHistoryList: List<Track>)
    fun clearSearchHistory()
}