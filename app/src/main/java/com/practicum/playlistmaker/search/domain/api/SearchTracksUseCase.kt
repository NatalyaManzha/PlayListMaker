package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.api.SearchTracksResultConsumer

interface SearchTracksUseCase {
    fun execute(
        expression: String,
        consumer: SearchTracksResultConsumer
    )
}