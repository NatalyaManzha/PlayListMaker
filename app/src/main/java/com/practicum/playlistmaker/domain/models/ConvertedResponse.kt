package com.practicum.playlistmaker.domain.models

import com.practicum.playlistmaker.player.domain.models.Track

data class ConvertedResponse(
    val results: List<Track>?,
    val state: SearchState
)