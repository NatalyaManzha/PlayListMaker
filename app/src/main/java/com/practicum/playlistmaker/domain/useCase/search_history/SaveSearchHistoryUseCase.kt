package com.practicum.playlistmaker.domain.useCase.search_history

import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class SaveSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    fun execute (searchHistoryList:List<Track>) {
        searchHistoryRepository.saveSearchHistory(searchHistoryList)
    }
}