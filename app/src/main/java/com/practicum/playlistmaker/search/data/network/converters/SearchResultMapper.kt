package com.practicum.playlistmaker.search.data.network.converters

import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.data.network.dto.TrackDto

object SearchResultMapper {
    fun map(result: List<TrackDto>): List<Track> {
        return result.map { it ->
            TrackDtoToTrackConverter.convert(it)
        }
    }
}