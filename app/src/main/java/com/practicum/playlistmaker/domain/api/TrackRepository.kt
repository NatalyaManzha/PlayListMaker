package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.ConvertedResponse

interface TrackRepository {
    fun searchTracks (expression: String): ConvertedResponse
}