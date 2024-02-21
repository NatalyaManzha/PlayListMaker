package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.network.api.ITunesSearchApi
import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.dto.Response
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchRequest

class NetworkClientImpl(
    private val iTunesService: ITunesSearchApi
) : NetworkClient {

    override fun doRequest(request: TrackSearchRequest): Response {
        return try {
            val response = iTunesService.search(request.expression).execute()
            val networkResponse = response.body() ?: Response()
            networkResponse.apply { stateCode = response.code() }
        } catch (ex: Exception) {
            Response().apply { stateCode = 400 }
        }
    }
}