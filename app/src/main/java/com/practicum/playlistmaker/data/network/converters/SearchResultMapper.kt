package com.practicum.playlistmaker.data.network.converters

import com.practicum.playlistmaker.data.network.dto.TrackDto
import com.practicum.playlistmaker.player.domain.models.Track

object SearchResultMapper {
    fun map(result: List<TrackDto>): List<Track> {
        return result.map { it ->
            TrackDtoToTrackConverter.convert(it)
        }
    }
}