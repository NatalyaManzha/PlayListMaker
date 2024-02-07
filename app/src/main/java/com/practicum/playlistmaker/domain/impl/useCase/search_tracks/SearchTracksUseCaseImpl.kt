package com.practicum.playlistmaker.domain.impl.useCase.search_tracks

import com.practicum.playlistmaker.domain.api.SearchTracksResultConsumer
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.useCase.SearchTracksUseCase
import java.util.concurrent.Executors

class SearchTracksUseCaseImpl(val trackRepository: TrackRepository):
    SearchTracksUseCase {
    private val executor = Executors.newCachedThreadPool()
    override fun execute(expression: String, consumer: SearchTracksResultConsumer) {
        executor.execute {
        consumer.consume(trackRepository.searchTracks(expression))
    }
}}