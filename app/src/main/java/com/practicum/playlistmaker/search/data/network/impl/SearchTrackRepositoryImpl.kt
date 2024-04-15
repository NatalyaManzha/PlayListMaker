package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.converters.SearchResultMapper
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.domain.models.SearchStateCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTrackRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchTrackRepository {

    override fun searchTracks(expression: String): Flow<ConvertedResponse> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        val convertedResponse =
            when (response.stateCode) {
                SearchStateCode.FAILURE -> ConvertedResponse(null, SearchState.FAILURE)
                SearchStateCode.SUCCESS -> {
                    val resultList = (response as TrackSearchResponse).results
                    if (resultList.isEmpty()) ConvertedResponse(null, SearchState.EMPTY)
                    else {
                        val resultListConverted = SearchResultMapper.map(resultList)
                        ConvertedResponse(resultListConverted, SearchState.SUCCESS)
                    }
                }

                else -> {
                    ConvertedResponse(null, SearchState.FAILURE)
                }
            }
        emit(convertedResponse)
    }
}