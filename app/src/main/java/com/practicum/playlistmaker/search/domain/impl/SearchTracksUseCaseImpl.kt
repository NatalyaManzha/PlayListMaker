package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import kotlinx.coroutines.flow.Flow

class SearchTracksUseCaseImpl(
    private val searchTrackRepository: SearchTrackRepository
) : SearchTracksUseCase {

    override fun execute(expression: String): Flow<ConvertedResponse> {
        return searchTrackRepository.searchTracks(expression)
    }
}