package com.practicum.playlistmaker.medialibrary.domain.impl

import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {
    override suspend fun insertPlaylist(playlist: NewPlaylist): Long {
        return playlistsRepository.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistID: Long) {
        playlistsRepository.deletePlaylist(playlistID)
    }

    override fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>> {
        return playlistsRepository.getPlaylistPreviewFlow()
    }

    override suspend fun updatePlaylist(playlistID: Long, count: Int) {
        playlistsRepository.updatePlaylist(playlistID, count)
    }

    override suspend fun addTrackToPlaylist(playlistID: Long, track: Track): Boolean {
       return playlistsRepository.addTrackToPlaylist(playlistID, track)
    }
}