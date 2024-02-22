package com.practicum.playlistmaker.search.data.local.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository {

    override fun getSearchHistoryList(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null)
        return if (json != null) createTrackListFromJson(json) else emptyList<Track>()
    }

    override fun saveSearchHistory(searchHistoryList: List<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, createJsonFromTrackList(searchHistoryList))
            .apply()
    }

    override fun clearSearchHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY)
            .apply()
    }

    private fun createTrackListFromJson(json: String): List<Track> {
        return gson.fromJson(json, Array<Track>::class.java).asList()

    }

    private fun createJsonFromTrackList(tracks: List<Track>): String {
        return gson.toJson(tracks)
    }

    companion object {
        const val SEARCH_HISTORY = "search_history"
    }
}