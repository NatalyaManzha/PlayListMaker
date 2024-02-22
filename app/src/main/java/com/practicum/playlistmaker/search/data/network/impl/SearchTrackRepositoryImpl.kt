package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.converters.SearchResultMapper
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.domain.models.SearchStateCode

class SearchTrackRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchTrackRepository {
    override fun searchTracks(expression: String): ConvertedResponse {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response !is TrackSearchResponse || (response.stateCode != SearchStateCode.SUCCESS)) {
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