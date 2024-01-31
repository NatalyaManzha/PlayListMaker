package com.practicum.playlistmaker.domain.impl.useCase.search_history


import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.useCase.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.domain.models.Track

class GetSearchHistoryListUseCaseImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) : GetSearchHistoryListUseCase {
    override fun execute(): MutableList<Track> =
        searchHistoryRepository.getSearchHistoryList().toMutableList()
}


