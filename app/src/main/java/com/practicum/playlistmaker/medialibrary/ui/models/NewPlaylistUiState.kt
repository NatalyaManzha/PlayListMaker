package com.practicum.playlistmaker.medialibrary.ui.models

import android.net.Uri

interface NewPlaylistUiState {
    object Default: NewPlaylistUiState
    object SavePlaylistEnabled: NewPlaylistUiState
    object SavePlaylistDisabled: NewPlaylistUiState

    data class SetNewImage( val uri: Uri): NewPlaylistUiState
}

data class NewPlaylistUiState2(
    val uri: Uri?,
    val showDialog: Boolean,
    val saveEnabled: Boolean
)