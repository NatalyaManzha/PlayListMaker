package com.practicum.playlistmaker.search.ui.converters

import com.practicum.playlistmaker.player.domain.models.Track

class TracklistMapper {
    companion object {
        fun map(
            tracklist: List<Track>,
            idList: List<Int>
        ): MutableList<Track> {
            val tracksInFavoritesList = mutableListOf<Track>()
            val tracksNotInFavoritesList = mutableListOf<Track>()
            val resultList = mutableListOf<Track>()

            val commonIds = tracklist.map { it.trackId }.intersect(idList.toSet()).reversed()
            tracklist.map {
                if (commonIds.contains(it.trackId))
                    tracksInFavoritesList.add(it.copy(inFavorite = true))
                else tracksNotInFavoritesList.add(it.copy(inFavorite = false))
            }
            commonIds.map { id ->
                resultList.add(tracksInFavoritesList.find { it.trackId == id }!!)
            }
            resultList.addAll(tracksNotInFavoritesList)
            return resultList.toMutableList()
        }
    }
}