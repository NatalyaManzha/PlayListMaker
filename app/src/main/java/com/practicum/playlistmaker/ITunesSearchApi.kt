package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


class TrackResponse(
    val resultCount: Int,
    val results: List<Track>
)

interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}
