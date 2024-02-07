package com.practicum.playlistmaker.domain.models

 data class ConvertedResponse(
    val results: List<Track>?,
    val state: SearchState
)