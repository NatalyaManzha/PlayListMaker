package com.practicum.playlistmaker.medialibrary.ui.newplaylist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.models.NewPlaylist
import com.practicum.playlistmaker.medialibrary.ui.models.NewPlaylistUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.NewPlaylistUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        NewPlaylistUiState(
            uri = null,
            showDialog = false,
            saveEnabled = false,
            saveCompletedSuccessfully = null,
            playlistName = null
        )
    )
    val uiStateFlow = _uiState.asStateFlow()

    private var name = STRING_DEFAULT_VALUE
    private var description = STRING_DEFAULT_VALUE
    private var iconUri: Uri? = null

    fun onUiEvent(event: NewPlaylistUiEvent) {
        when (event) {
            NewPlaylistUiEvent.OnCreateButtonClick -> savePlaylist()
            is NewPlaylistUiEvent.ImageChanged -> onImageChanged(event.uri)
            is NewPlaylistUiEvent.EditText -> saveChanges(event.flag, event.s)
        }
    }

    private fun onImageChanged(uri: Uri) {
        iconUri = uri
        _uiState.value = _uiState.value.copy(uri = uri, showDialog = true)
    }


    private fun saveChanges(flag: Boolean, s: CharSequence?) {
        if (flag) onEditName(s)
        else onEditDescription(s)
    }

    private fun onEditName(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            name = STRING_DEFAULT_VALUE
            _uiState.value = _uiState.value.copy(saveEnabled = false)
        } else {
            name = s.toString()
            _uiState.value = _uiState.value.copy(saveEnabled = true)
        }
        checkData()
    }

    private fun onEditDescription(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            description = STRING_DEFAULT_VALUE
        } else {
            description = s.toString()
        }
        checkData()
    }

    private fun checkData() {
        val showDialog =
            !(iconUri == null
                    && description == STRING_DEFAULT_VALUE
                    && name == STRING_DEFAULT_VALUE)
        _uiState.value = _uiState.value.copy(showDialog = showDialog)
    }

    private fun savePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = playlistsInteractor.insertPlaylist(
                NewPlaylist(
                    iconUri = iconUri,
                    name = name,
                    description = description
                )
            )
            val saveCompletedSuccessfully = (result != SAVE_FAILURE_CODE)
            _uiState.value = _uiState.value.copy(
                saveCompletedSuccessfully = saveCompletedSuccessfully,
                playlistName = name
            )
        }
    }

    companion object {
        private const val STRING_DEFAULT_VALUE = ""
        private const val SAVE_FAILURE_CODE = -1L
    }
}