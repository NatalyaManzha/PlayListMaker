package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchTracksResultConsumer
import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.ui.models.SingleLiveEvent
import com.practicum.playlistmaker.search.ui.models.UiState

class SearchViewModel : ViewModel() {
    private var uiStateLiveData = MutableLiveData<UiState>()
    private var searchHistoryList: MutableList<Track>
    private var clearTextEnabledLiveData = MutableLiveData<Boolean>()
    private var clearTextLiveData = SingleLiveEvent<Boolean>()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { startSearch() }
    private lateinit var searchRequest: String


    init {
        val useCase = Creator.provideGetSearchHistoryListUseCase()
        searchHistoryList = useCase.execute()
        uiStateLiveData.value = UiState.Default
    }

    fun observeState(): LiveData<UiState> = uiStateLiveData
    fun observeClearTextEnabled(): LiveData<Boolean> = clearTextEnabledLiveData
    fun observeClearText(): LiveData<Boolean> = clearTextLiveData
    fun clearSearchHistory() {
        val useCase = Creator.provideClearSearchHistoryUseCase()
        useCase.execute()
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
        val useCase = Creator.provideSaveSearchHistoryUseCase()
        useCase.execute(searchHistoryList)
    }

    fun startSearch() {
        uiStateLiveData.value = UiState.Loading
        Creator.provideSearchTracksUseCase()
            .execute(searchRequest, object : SearchTracksResultConsumer {
                override fun consume(result: ConvertedResponse) {
                        when (result.state) {
                            SearchState.FAILURE -> uiStateLiveData.postValue(UiState.Error)
                            SearchState.EMPTY -> uiStateLiveData.postValue(UiState.EmptyResult)
                            SearchState.SUCCESS -> {
                                uiStateLiveData.postValue(UiState.SearchResult(result.results!!))
                                }
                            }
                        }
                }
            )
    }

    fun onActivityPause() {
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)
    }

    fun searchDebounce() {
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)
        handler.postDelayed(
            searchRunnable,
            SEARCH_DEBOUNCE_TOKEN,
            SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    companion object {
        private val SEARCH_DEBOUNCE_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val MAX_LIST_SIZE = 10
    }
}