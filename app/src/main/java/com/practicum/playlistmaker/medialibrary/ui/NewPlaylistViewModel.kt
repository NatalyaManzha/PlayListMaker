package com.practicum.playlistmaker.medialibrary.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.models.Playlist
import com.practicum.playlistmaker.medialibrary.ui.models.NewPlaylistUiEvent
import com.practicum.playlistmaker.medialibrary.ui.models.NewPlaylistUiState2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        NewPlaylistUiState2(
            uri = null,
            showDialog = false,
            saveEnabled = false
        )
    )
    val uiStateFlow = _uiState.asStateFlow()

    private var name = STRING_DEFAULT_VALUE
    private var description = STRING_DEFAULT_VALUE
    private val iconUrl = STRING_DEFAULT_VALUE
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

    private fun saveImage(uri: Uri) {
        //сгенерировать имя для файла
        //сохранение картинки в отдельную папку
        //получить адрес в хранилище
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
            playlistsInteractor.insertPlaylist(
                Playlist(
                    id = null,
                    iconUrl = iconUrl,
                    name = name,
                    description = description,
                    count = INT_DEFAULT_VALUE
                )
            )
        }
    }

    companion object {
        private const val STRING_DEFAULT_VALUE = ""
        private const val INT_DEFAULT_VALUE = 0
    }
}