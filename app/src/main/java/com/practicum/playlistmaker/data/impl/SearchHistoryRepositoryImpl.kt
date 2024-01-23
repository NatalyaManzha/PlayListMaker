package com.practicum.playlistmaker.data.impl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.PREFERENCES
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {
    val sharedPreferences =
        context.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)

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
        return Gson().fromJson(json, Array<Track>::class.java).asList()

    }

    private fun createJsonFromTrackList(tracks: List<Track>): String {
        return Gson().toJson(tracks)
    }

    companion object {
        const val SEARCH_HISTORY = "search_history"
    }
}