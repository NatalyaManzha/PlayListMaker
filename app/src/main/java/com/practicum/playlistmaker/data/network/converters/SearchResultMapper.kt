package com.practicum.playlistmaker.data.network.converters

import com.practicum.playlistmaker.data.network.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track

object SearchResultMapper {
    val converter = TrackDtoToTrackConverter()
    fun map(result: List<TrackDto>): List<Track> {
        return result.map { it ->
            converter.convert(it)
        }
    }
}