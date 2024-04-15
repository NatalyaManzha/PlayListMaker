package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.network.api.ConnectivityCheck
import com.practicum.playlistmaker.search.data.network.api.ITunesSearchApi
import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.dto.Response
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.domain.models.SearchStateCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkClientImpl(
    private val iTunesService: ITunesSearchApi,
    private val connectivityCheck: ConnectivityCheck
) : NetworkClient {

    override suspend fun doRequest(request: TrackSearchRequest): Response {
        if (connectivityCheck.isConnected() == false) {
            return Response().apply { stateCode = SearchStateCode.FAILURE }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = iTunesService.search(request.expression)
                response.apply { stateCode = SearchStateCode.SUCCESS }
            } catch (ex: Exception) {
                Response().apply { stateCode = SearchStateCode.FAILURE }
            }
        }
    }
}