package com.practicum.playlistmaker.domain.api.useCase

import com.practicum.playlistmaker.domain.models.Track

interface GetSearchHistoryListUseCase {
    fun execute(): MutableList<Track>
}