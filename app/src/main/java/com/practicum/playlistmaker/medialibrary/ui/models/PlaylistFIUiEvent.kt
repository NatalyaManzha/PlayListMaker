package com.practicum.playlistmaker.medialibrary.ui.models

sealed interface PlaylistFIUiEvent {
    data class OnCreateView(val playlistID: Long) : PlaylistFIUiEvent
}