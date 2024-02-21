package com.practicum.playlistmaker.search.domain.api

interface SearchTracksUseCase {
    fun execute(
        expression: String,
        consumer: SearchTracksResultConsumer
    )
}