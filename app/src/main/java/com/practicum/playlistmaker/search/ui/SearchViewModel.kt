package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchTracksResultConsumer
import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.ui.models.SingleLiveEvent
import com.practicum.playlistmaker.search.ui.models.UiState
import com.practicum.playlistmaker.utils.Creator

class SearchViewModel : ViewModel() {

    private lateinit var searchRequest: String
    private var uiStateLiveData = MutableLiveData<UiState>()
    private var searchHistoryList: MutableList<Track>
    private var clearTextEnabledLiveData = MutableLiveData<Boolean>()
    private var clearTextLiveData = SingleLiveEvent<Boolean>()
    private val handler = Handler(Looper.getMainLooper())

    init {
        searchHistoryList = Creator.provideGetSearchHistoryListUseCase().execute()
        uiStateLiveData.value = UiState.Default
    }

    fun observeState(): LiveData<UiState> = uiStateLiveData
    fun observeClearTextEnabled(): LiveData<Boolean> = clearTextEnabledLiveData
    fun observeClearText(): LiveData<Boolean> = clearTextLiveData
    fun clearSearchHistory() {
        Creator.provideClearSearchHistoryUseCase().execute()
        uiStateLiveData.value = UiState.ClearSearchHistory
        searchHistoryList.clear()
    }

    fun showSearchhistory(hasFocus: Boolean, s: CharSequence?) {
        if (hasFocus && s?.isEmpty() == true && searchHistoryList.size > 0)
            uiStateLiveData.value = UiState.SearchHistory(searchHistoryList)
    }

    fun addTrack(track: Track) {
        with(searchHistoryList) {
            remove(track)
            add(0, track)
            if (size > MAX_LIST_SIZE) remove(this[MAX_LIST_SIZE])
        }
    }

    fun onTextChanged(s: CharSequence?) {
        val enabled = !s.isNullOrEmpty()
        clearTextEnabledLiveData.value = enabled
        searchRequest = s.toString()
    }

    fun onTextCleared() {
        clearTextLiveData.value = true
    }

    fun saveSearchHistory() {
        Creator.provideSaveSearchHistoryUseCase().execute(searchHistoryList)
    }

    fun startSearch() {
        if (!searchRequest.isNullOrEmpty()) {
            uiStateLiveData.value = UiState.Loading
            Creator.provideSearchTracksUseCase()
                .execute(searchRequest, object : SearchTracksResultConsumer {
                    override fun consume(result: ConvertedResponse) {
                        val uiState =
                            when (result.state) {
                                SearchState.FAILURE -> UiState.Error
                                SearchState.EMPTY -> UiState.EmptyResult
                                SearchState.SUCCESS -> UiState.SearchResult(result.results!!)
                            }
                        renderUiState(uiState)
                    }
                })
        }
    }

    fun renderUiState(uiState: UiState) {
        uiStateLiveData.postValue(uiState)
    }

    fun onActivityPause() {
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)
    }

    fun searchDebounce() {
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)
        val runnable = { startSearch() }
        handler.postDelayed(
            runnable,
            SEARCH_DEBOUNCE_TOKEN,
            SEARCH_DEBOUNCE_DELAY_MILLIS
        )
    }

    companion object {
        private val SEARCH_DEBOUNCE_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val MAX_LIST_SIZE = 10
    }
}