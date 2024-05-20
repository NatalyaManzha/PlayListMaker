package com.practicum.playlistmaker.medialibrary.ui.models

import android.net.Uri

data class EditPlaylistUiState(
    val uri: Uri?,
    val saveEnabled: Boolean
)
