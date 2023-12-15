package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class SearchHistory(context: Context) {

    val sharedPreferences =
        context.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)

    fun setTrackToPlay(track: Track) {
        sharedPreferences.edit()
            .putString(TRACK_TO_PLAY, Gson().toJson(track))
            .apply()
    }


    fun getSearchList(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null)
        return if (json != null) createTrackListFromJson(json) else emptyList<Track>()
    }

    fun addTrack(track: Track, adapter: TrackListAdapter) {
        if (adapter.trackList.contains(track)) {
            val index = adapter.trackList.indexOf(track)
            adapter.trackList.remove(track)
            adapter.notifyItemRemoved(index)
            adapter.notifyItemRangeChanged(index, adapter.trackList.size - 1)
        }

        adapter.trackList.add(0, track)
        adapter.notifyItemInserted(0)
        if (adapter.trackList.size == MAX_LIST_SIZE + 1) {
            adapter.trackList.remove(adapter.trackList[MAX_LIST_SIZE])
            adapter.notifyItemRemoved(10)
        }
    }

    fun saveSearchHistory(searchList: List<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, createJsonFromTrackList(searchList))
            .apply()
    }

    fun clearSearchHistory(searchList: MutableList<Track>) {
        searchList.clear()
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