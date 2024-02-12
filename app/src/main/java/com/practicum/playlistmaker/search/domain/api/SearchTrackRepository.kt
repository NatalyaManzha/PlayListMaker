package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ConvertedResponse

interface SearchTrackRepository {
    fun searchTracks(expression: String): ConvertedResponse
}