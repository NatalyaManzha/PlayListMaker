package com.practicum.playlistmaker.data.network.dto

class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()