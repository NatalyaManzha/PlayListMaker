package com.practicum.playlistmaker.data.network.impl

import com.practicum.playlistmaker.data.network.api.NetworkClient
import com.practicum.playlistmaker.data.network.converters.SearchResultMapper
import com.practicum.playlistmaker.data.network.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.network.dto.TrackSearchResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.ConvertedResponse
import com.practicum.playlistmaker.domain.models.SearchState

class TrackRepositoryImpl(
    private val networkClient: NetworkClient
) : TrackRepository {
    override fun searchTracks(expression: String): ConvertedResponse {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response !is TrackSearchResponse || (response.stateCode != 200)) {
            return ConvertedResponse(null, SearchState.FAILURE)
        }
        val resultList = response.results
        if (resultList.isEmpty()) return ConvertedResponse(null, SearchState.EMPTY)
        else {
            val resultListConverted = SearchResultMapper.map(resultList)
            return ConvertedResponse(resultListConverted, SearchState.SUCCESS)
        }
    }
}