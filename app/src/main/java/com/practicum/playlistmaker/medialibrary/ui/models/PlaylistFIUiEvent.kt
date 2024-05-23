package com.practicum.playlistmaker.medialibrary.ui.models

sealed interface PlaylistFIUiEvent {
    data class OnCreateView(val playlistID: Long) : PlaylistFIUiEvent
    data class OnTrackClick(val trackID: Int) : PlaylistFIUiEvent
    data class DeleteTrack(val trackID: Int) : PlaylistFIUiEvent
    object SharePlaylist : PlaylistFIUiEvent
    object DeletePlaylist : PlaylistFIUiEvent
}