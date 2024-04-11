package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import kotlinx.coroutines.flow.Flow

interface SearchTrackRepository {
    fun searchTracks(expression: String): Flow<ConvertedResponse>
}