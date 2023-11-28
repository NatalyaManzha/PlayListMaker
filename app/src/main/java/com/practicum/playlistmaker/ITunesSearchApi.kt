package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class Track (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)

class TrackResponse (
    val resultCount: Int,
    val results: List <Track>
    )

interface ITunesSearchApi {
    @GET ("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}