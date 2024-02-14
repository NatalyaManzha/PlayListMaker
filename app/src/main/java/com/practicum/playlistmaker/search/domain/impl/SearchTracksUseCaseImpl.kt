package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.search.domain.api.SearchTracksResultConsumer
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import java.util.concurrent.Executors

class SearchTracksUseCaseImpl(val searchTrackRepository: SearchTrackRepository) :
    SearchTracksUseCase {
    private val executor = Executors.newCachedThreadPool()
    override fun execute(expression: String, consumer: SearchTracksResultConsumer) {
        executor.execute {
            consumer.consume(searchTrackRepository.searchTracks(expression))
        }
    }
}