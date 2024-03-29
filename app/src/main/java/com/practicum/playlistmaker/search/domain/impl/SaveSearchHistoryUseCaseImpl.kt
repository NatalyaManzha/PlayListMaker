package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository

class SaveSearchHistoryUseCaseImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) : SaveSearchHistoryUseCase {
    override fun execute(searchHistoryList: List<Track>) {
        searchHistoryRepository.saveSearchHistory(searchHistoryList)
    }
}