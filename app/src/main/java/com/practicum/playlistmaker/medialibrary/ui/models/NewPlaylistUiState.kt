package com.practicum.playlistmaker.medialibrary.ui.models

import android.net.Uri

data class NewPlaylistUiState2(
    val uri: Uri?,
    val showDialog: Boolean,
    val saveEnabled: Boolean,
    val saveCompletedSuccessfully: Boolean?,
    val playlistName: String?
)