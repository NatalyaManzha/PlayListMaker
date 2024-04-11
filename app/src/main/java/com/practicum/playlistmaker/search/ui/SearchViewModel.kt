package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.search.domain.api.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.models.ConvertedResponse
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.ui.models.SingleLiveEvent
import com.practicum.playlistmaker.search.ui.models.UiEvent
import com.practicum.playlistmaker.search.ui.models.UiState
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getSearchHistoryListUseCase: GetSearchHistoryListUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private var lastSearchRequest: String? = null
    private var uiStateLiveData = MutableLiveData<UiState>()
    private var searchHistoryList: MutableList<Track>
    private var clearTextEnabledLiveData = MutableLiveData<Boolean>()
    private var clearTextLiveData = SingleLiveEvent<Boolean>()
    private var searchJob: Job? = null
    private val searchTrackDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY_MILLIS,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { searchRequest ->
        startSearch(searchRequest)
    }

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

    fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.FocusChanged -> showSearchhistory(event.hasFocus, event.s)
            is UiEvent.ActionDone -> startSearch(event.s.toString())
            is UiEvent.BeforeTextChanged -> clearTextEnable(event.s)
            is UiEvent.OnTextChanged -> {
                clearTextEnable(event.s)
                showSearchhistory(event.hasFocus, event.s)
                searchDebounce(event.s.toString())
            }

            is UiEvent.UpdateButtonClick -> {
                lastSearchRequest = null
                startSearch(event.s.toString())
            }

            is UiEvent.ClearButtonClick -> onTextCleared()
            is UiEvent.ClearHistoryButtonClick -> clearSearchHistory()
            is UiEvent.AddTrack -> addTrack(event.track)
            is UiEvent.ShowSearchResult -> clearTextEnable(event.s)
            is UiEvent.OnStop -> saveSearchHistory()
        }
    }

    private fun showSearchhistory(hasFocus: Boolean, s: CharSequence?) {
        if (hasFocus && s?.isEmpty() == true && searchHistoryList.size > 0)
            uiStateLiveData.value = UiState.SearchHistory(searchHistoryList)
    }

    private fun addTrack(track: Track) {
        with(searchHistoryList) {
            remove(track)
            add(0, track)
            if (size > MAX_LIST_SIZE) remove(this[MAX_LIST_SIZE])
        }
    }

    private fun clearTextEnable(s: CharSequence?) {
        val enabled = !s.isNullOrEmpty()
        clearTextEnabledLiveData.value = enabled
    }

    private fun onTextCleared() {
        clearTextLiveData.value = true
    }

    private fun saveSearchHistory() {
        saveSearchHistoryUseCase.execute(searchHistoryList)
    }

    private fun startSearch(searchRequest: String) {
        if (!searchRequest.isNullOrEmpty() && (searchRequest != lastSearchRequest)) {
            uiStateLiveData.value = UiState.Loading
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                searchTracksUseCase.execute(searchRequest)
                    .collect { result ->
                        handleSearchResult(result)
                    }
                lastSearchRequest = searchRequest
            }
        }
    }

    private fun handleSearchResult(result: ConvertedResponse) {
        val uiState =
            when (result.state) {
                SearchState.FAILURE -> UiState.Error
                SearchState.EMPTY -> UiState.EmptyResult
                SearchState.SUCCESS -> UiState.SearchResult(result.results!!)
            }
        renderUiState(uiState)
    }

    private fun renderUiState(uiState: UiState) {
        if (uiStateLiveData.value is UiState.Loading) uiStateLiveData.postValue(uiState)
    }

    private fun searchDebounce(searchRequest: String) {
        searchTrackDebounce(searchRequest)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val MAX_LIST_SIZE = 10
    }
}