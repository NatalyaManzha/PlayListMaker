package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ConvertedResponse

interface SearchTracksResultConsumer {
    fun consume(result: ConvertedResponse)
}