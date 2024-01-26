package com.practicum.playlistmaker.domain.impl.useCase.search_history


import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.useCase.GetSearchHistoryList
import com.practicum.playlistmaker.domain.models.Track

class GetSearchHistoryListUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) : GetSearchHistoryList {
    override fun execute(): List<Track> =
        searchHistoryRepository.getSearchHistoryList()
}


