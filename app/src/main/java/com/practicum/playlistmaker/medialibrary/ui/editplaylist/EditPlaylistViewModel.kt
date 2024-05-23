package com.practicum.playlistmaker.medialibrary.ui.editplaylist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.models.EditPlaylist
import com.practicum.playlistmaker.medialibrary.ui.models.EditPlaylistUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.EditPlaylistUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class EditPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private var playlistID by Delegates.notNull<Long>()
    private var playlistName = STRING_DEFAULT_VALUE
    private var playlistDescription = STRING_DEFAULT_VALUE
    private var playlistIconUri: Uri? = null
    private var acceptValuesAllowed = true

    private val _uiState = MutableStateFlow(
        EditPlaylistUiState(
            uri = null,
            saveEnabled = true
        )
    )
    val uiStateFlow = _uiState.asStateFlow()


    fun onUiEvent(event: EditPlaylistUiEvent) {
        when (event) {
            EditPlaylistUiEvent.OnSaveButtonClick -> saveEditedPlaylist()
            is EditPlaylistUiEvent.ImageChanged -> onImageChanged(event.uri)
            is EditPlaylistUiEvent.EditText -> saveChanges(event.flag, event.s)
            is EditPlaylistUiEvent.InitialValues -> setInitialValues(event.playlistInfo)
        }
    }

    private fun setInitialValues(playlistInfo: EditPlaylist) {
        if (acceptValuesAllowed) {
            with(playlistInfo) {
                playlistID = id
                playlistName = name
                playlistDescription = description
                iconUri?.let {
                    playlistIconUri = it
                    _uiState.value = _uiState.value.copy(uri = it)
                }
            }
        }
        acceptValuesAllowed = false
    }

    private fun onImageChanged(uri: Uri) {
        playlistIconUri = uri
        _uiState.value = _uiState.value.copy(uri = uri)
    }

    private fun saveChanges(flag: Boolean, s: CharSequence?) {
        if (flag) onEditName(s)
        else onEditDescription(s)
    }

    private fun onEditName(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            playlistName = STRING_DEFAULT_VALUE
            _uiState.value = _uiState.value.copy(saveEnabled = false)
        } else {
            playlistName = s.toString()
            _uiState.value = _uiState.value.copy(saveEnabled = true)
        }
    }

    private fun onEditDescription(s: CharSequence?) {
        playlistDescription = if (s.isNullOrEmpty()) STRING_DEFAULT_VALUE
        else s.toString()
    }

    private fun saveEditedPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.updatePlaylist(
                EditPlaylist(
                    playlistID,
                    playlistIconUri,
                    playlistName,
                    playlistDescription
                )
            )
        }
    }

    companion object {
        private const val STRING_DEFAULT_VALUE = ""
    }
}