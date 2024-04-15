package com.practicum.playlistmaker.search.data.network.api

import com.practicum.playlistmaker.search.data.network.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ITunesSearchApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") expression: String): TrackSearchResponse
}
