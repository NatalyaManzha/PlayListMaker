package com.practicum.playlistmaker.medialibrary.domain.impl

import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsRepository
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistInfo
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview
import com.practicum.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {
    override suspend fun insertPlaylist(playlist: NewPlaylist): Long {
        return playlistsRepository.insertPlaylist(playlist)
    }

    override fun getPlaylistPreviewFlow(): Flow<List<PlaylistPreview>> {
        return playlistsRepository.getPlaylistPreviewFlow()
    }

    override suspend fun addTrackToPlaylist(playlistID: Long, track: Track): Boolean {
        return playlistsRepository.addTrackToPlaylist(playlistID, track)
    }

    override fun getPlaylistInfoFlow(playlistID: Long): Flow<PlaylistInfo> {
       return playlistsRepository.getPlaylistInfoFlow(playlistID)
    }

    override fun getTrackIdListFlow(playlistId: Long): Flow<List<Int>> {
        return playlistsRepository.getTrackIdListFlow(playlistId)
    }

    override suspend fun getTrackByID(trackId: Int): Track {
        return playlistsRepository.getTrackByID(trackId)
    }
}