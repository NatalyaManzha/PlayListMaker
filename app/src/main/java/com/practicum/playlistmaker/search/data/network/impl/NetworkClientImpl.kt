package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.network.api.ConnectivityTest
import com.practicum.playlistmaker.search.data.network.api.ITunesSearchApi
import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.dto.Response
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.domain.models.SearchStateCode

class NetworkClientImpl(
    private val iTunesService: ITunesSearchApi,
    private val connectivityTest: ConnectivityTest
) : NetworkClient {

    override fun doRequest(request: TrackSearchRequest): Response {
        if (connectivityTest.isConnected() == false) {
            return Response().apply { stateCode = SearchStateCode.FAILURE }
        }
        return try {
            val response = iTunesService.search(request.expression).execute()
            val networkResponse = response.body() ?: Response()
            networkResponse.apply { stateCode = response.code() }
        } catch (ex: Exception) {
            Response().apply { stateCode = SearchStateCode.FAILURE }
        }
    }
}