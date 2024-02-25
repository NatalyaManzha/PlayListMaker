package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.search.domain.api.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.api.SearchTracksResultConsumer
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.ui.models.SingleLiveEvent
import com.practicum.playlistmaker.search.ui.models.UiState

class SearchViewModel(
    private val handler: Handler,
    private val getSearchHistoryListUseCase: GetSearchHistoryListUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private lateinit var searchRequest: String
    private var uiStateLiveData = MutableLiveData<UiState>()
    private var searchHistoryList: MutableList<Track>
    private var clearTextEnabledLiveData = MutableLiveData<Boolean>()
    private var clearTextLiveData = SingleLiveEvent<Boolean>()

    init {
        searchHistoryList = getSearchHistoryListUseCase.execute()
        uiStateLiveData.value = UiState.Default
    }

    fun observeState(): LiveData<UiState> = uiStateLiveData
    fun observeClearTextEnabled(): LiveData<Boolean> = clearTextEnabledLiveData
    fun observeClearText(): LiveData<Boolean> = clearTextLiveData
    fun clearSearchHistory() {
        clearSearchHistoryUseCase.execute()
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
        saveSearchHistoryUseCase.execute(searchHistoryList)
    }

    fun startSearch() {
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)
        if (!searchRequest.isNullOrEmpty()) {
            uiStateLiveData.value = UiState.Loading
            searchTracksUseCase.execute(searchRequest, object : SearchTracksResultConsumer {
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
        if (uiStateLiveData.value is UiState.Loading)
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