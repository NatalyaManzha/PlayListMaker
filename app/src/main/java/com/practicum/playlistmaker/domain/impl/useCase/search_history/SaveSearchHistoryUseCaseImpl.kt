package com.practicum.playlistmaker.domain.impl.useCase.search_history

import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.useCase.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.domain.models.Track

class SaveSearchHistoryUseCaseImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) : SaveSearchHistoryUseCase {
    override fun execute(searchHistoryList: List<Track>) {
        searchHistoryRepository.saveSearchHistory(searchHistoryList)
    }
}