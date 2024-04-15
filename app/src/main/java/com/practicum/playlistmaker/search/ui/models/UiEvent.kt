package com.practicum.playlistmaker.search.ui.models

import com.practicum.playlistmaker.player.domain.models.Track

sealed interface UiEvent {
    data class FocusChanged(val hasFocus: Boolean, val s: CharSequence?) : UiEvent
    data class ActionDone(val s: CharSequence?) : UiEvent
    data class BeforeTextChanged(val s: CharSequence?) : UiEvent
    data class OnTextChanged(val hasFocus: Boolean, val s: CharSequence?) : UiEvent
    data class UpdateButtonClick(val s: CharSequence?) : UiEvent
    object ClearButtonClick : UiEvent
    object ClearHistoryButtonClick : UiEvent
    data class AddTrack(val track: Track) : UiEvent
    data class ShowSearchResult(val s: CharSequence?) : UiEvent
    object OnStop : UiEvent
}