package com.practicum.playlistmaker.domain.useCase

import com.practicum.playlistmaker.domain.api.SearchHistoryRepository

class ClearSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    fun execute () {
        searchHistoryRepository.clearSearchHistory()
    }
}