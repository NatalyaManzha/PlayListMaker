package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.Arrays

class SearchHistory(var sharedPreferences: SharedPreferences) {
    companion object {
        const val SEARCH_HISTORY = "search_history"
    }


    fun getSearchList(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null)
        return if (json != null) ArrayList(Arrays.asList(*createTrackListFromJson(json))) else ArrayList<Track>()
    }

    fun addTrack(track: Track) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_UPDATE, createJsonFromTrack(track))
            .apply()
    }


    fun saveSearchHistory(searchList: ArrayList<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, createJsonFromTrackList(searchList.toTypedArray()))
            .apply()
    }

    fun clearSearchHistory(searchList: ArrayList<Track>) {
        searchList.clear()
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY)
            .apply()
    }

    private fun createTrackListFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private fun createJsonFromTrackList(tracks: Array<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createJsonFromTrack(track: Track): String {
        return Gson().toJson(track)
    }

    private fun createTrackFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }

}