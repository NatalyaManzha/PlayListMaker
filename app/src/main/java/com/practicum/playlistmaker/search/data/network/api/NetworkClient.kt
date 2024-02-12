package com.practicum.playlistmaker.search.data.network.api

import com.practicum.playlistmaker.search.data.network.dto.Response
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchRequest

interface NetworkClient {
    fun doRequest(request: TrackSearchRequest): Response
}