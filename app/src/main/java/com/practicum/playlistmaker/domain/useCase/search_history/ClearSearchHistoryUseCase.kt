package com.practicum.playlistmaker.domain.useCase.search_history

import com.practicum.playlistmaker.domain.api.SearchHistoryRepository

class ClearSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    fun execute () {
        searchHistoryRepository.clearSearchHistory()
    }
}