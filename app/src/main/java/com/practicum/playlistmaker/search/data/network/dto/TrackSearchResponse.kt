package com.practicum.playlistmaker.search.data.network.dto

class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()