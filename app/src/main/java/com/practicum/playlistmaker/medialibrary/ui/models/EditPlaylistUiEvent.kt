package com.practicum.playlistmaker.medialibrary.ui.models

import android.net.Uri
import com.practicum.playlistmaker.medialibrary.domain.models.EditPlaylist

sealed interface EditPlaylistUiEvent {

    data class InitialValues(val playlistInfo: EditPlaylist) : EditPlaylistUiEvent
    object OnSaveButtonClick : EditPlaylistUiEvent

    data class EditText(val flag: Boolean, val s: CharSequence?) : EditPlaylistUiEvent

    data class ImageChanged(val uri: Uri) : EditPlaylistUiEvent
}