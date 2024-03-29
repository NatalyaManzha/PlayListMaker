package com.practicum.playlistmaker.search.domain.impl


import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository

class GetSearchHistoryListUseCaseImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) : GetSearchHistoryListUseCase {
    override fun execute(): MutableList<Track> =
        searchHistoryRepository.getSearchHistoryList().toMutableList()
}


