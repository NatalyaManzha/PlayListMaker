package com.practicum.playlistmaker.domain.api.useCase

import com.practicum.playlistmaker.domain.api.SearchTracksResultConsumer

interface SearchTracksUseCase {
    fun execute(
        expression: String,
        consumer: SearchTracksResultConsumer
    )
}