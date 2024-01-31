package com.practicum.playlistmaker.domain.impl.useCase.search_history

import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.useCase.ClearSearchHistoryUseCase

class ClearSearchHistoryUseCaseImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) : ClearSearchHistoryUseCase {
    override fun execute() {
        searchHistoryRepository.clearSearchHistory()
    }
}