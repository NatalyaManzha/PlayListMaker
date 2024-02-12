package com.practicum.playlistmaker.player.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.player.domain.api.FavoriteTracksRepository

class FavoriteTracksRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    FavoriteTracksRepository {
    override fun addToFavorites(trackId: String) {
        changeFavorites(trackId = trackId, remove = false)
    }

    override fun removeFromFavorites(trackId: String) {
        changeFavorites(trackId = trackId, remove = true)
    }

    override fun checkFavorites(trackId: String): Boolean {
        return getSavedFavorites().contains(trackId)
    }

    private fun changeFavorites(trackId: String, remove: Boolean) {
        val mutableSet = getSavedFavorites().toMutableSet()
        val modified = if (remove) mutableSet.remove(trackId) else mutableSet.add(trackId)
        if (modified) sharedPreferences.edit().putStringSet(FAVORITES_KEY, mutableSet).apply()
    }

    private fun getSavedFavorites(): Set<String> {
        return sharedPreferences.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }

    private companion object {
        const val FAVORITES_KEY = "FAVORITES_KEY"
    }
}