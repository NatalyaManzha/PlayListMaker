package com.practicum.playlistmaker.domain.api.useCase

import com.practicum.playlistmaker.domain.models.Track

interface SaveSearchHistoryUseCase {
    fun execute(searchHistoryList: List<Track>)
}