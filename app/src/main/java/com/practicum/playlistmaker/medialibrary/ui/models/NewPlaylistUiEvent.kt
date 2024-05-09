package com.practicum.playlistmaker.medialibrary.ui.models

import android.net.Uri

sealed interface NewPlaylistUiEvent {
    object OnCreateButtonClick: NewPlaylistUiEvent

    data class EditText( val flag:Boolean, val s: CharSequence?): NewPlaylistUiEvent

    data class ImageChanged(val uri: Uri): NewPlaylistUiEvent
}