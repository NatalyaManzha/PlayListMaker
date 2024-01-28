package com.practicum.playlistmaker.domain.impl.useCase.search_history

import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.useCase.SaveSearchHistory
import com.practicum.playlistmaker.domain.models.Track

class SaveSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) : SaveSearchHistory {
    override fun execute(searchHistoryList: List<Track>) {
        searchHistoryRepository.saveSearchHistory(searchHistoryList)
    }
}