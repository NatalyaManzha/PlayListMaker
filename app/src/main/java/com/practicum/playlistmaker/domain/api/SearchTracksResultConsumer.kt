package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.ConvertedResponse

interface SearchTracksResultConsumer {
    fun consume (result: ConvertedResponse)
}