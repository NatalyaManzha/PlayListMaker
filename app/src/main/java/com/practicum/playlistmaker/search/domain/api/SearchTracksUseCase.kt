package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import kotlinx.coroutines.flow.Flow

interface SearchTracksUseCase {

    fun execute(expression: String): Flow<ConvertedResponse>
}