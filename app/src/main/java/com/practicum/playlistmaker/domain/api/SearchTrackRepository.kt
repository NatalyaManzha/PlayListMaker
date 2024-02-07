package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.ConvertedResponse

interface SearchTrackRepository {
    fun searchTracks (expression: String): ConvertedResponse
}