package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.player.domain.models.Track

interface SaveSearchHistoryUseCase {
    fun execute(searchHistoryList: List<Track>)
}