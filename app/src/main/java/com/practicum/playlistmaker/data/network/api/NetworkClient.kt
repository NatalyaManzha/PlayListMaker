package com.practicum.playlistmaker.data.network.api

import com.practicum.playlistmaker.data.network.dto.Response
import com.practicum.playlistmaker.data.network.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.network.dto.TrackSearchResponse

interface NetworkClient {
    fun doRequest(request: TrackSearchRequest): Response
}