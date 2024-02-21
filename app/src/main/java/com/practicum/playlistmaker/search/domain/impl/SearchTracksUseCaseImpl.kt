package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.search.domain.api.SearchTracksResultConsumer
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import java.util.concurrent.ExecutorService

class SearchTracksUseCaseImpl(
    private val searchTrackRepository: SearchTrackRepository,
    private val executor: ExecutorService
) : SearchTracksUseCase {

    override fun execute(expression: String, consumer: SearchTracksResultConsumer) {
        executor.execute {
            consumer.consume(searchTrackRepository.searchTracks(expression))
        }
    }
}