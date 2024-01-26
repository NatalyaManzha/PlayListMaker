package com.practicum.playlistmaker.domain.impl.useCase.search_history

import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.useCase.ClearSearchHistory

class ClearSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) : ClearSearchHistory {
    override fun execute() {
        searchHistoryRepository.clearSearchHistory()
    }
}