package com.practicum.playlistmaker.medialibrary.ui.models

import android.net.Uri
import com.practicum.playlistmaker.player.domain.models.Track
sealed interface PlaylistFIUiEvent{
    data class OnCreateView(val playlistID: Long): PlaylistFIUiEvent
}