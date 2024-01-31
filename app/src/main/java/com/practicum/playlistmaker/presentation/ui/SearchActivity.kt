package com.practicum.playlistmaker.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.network.api.ITunesSearchApi
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.MAX_LIST_SIZE
import com.practicum.playlistmaker.domain.TRACK_TO_PLAY
import com.practicum.playlistmaker.domain.api.SearchTracksResultConsumer
import com.practicum.playlistmaker.domain.models.ConvertedResponse
import com.practicum.playlistmaker.domain.models.SearchState
import com.practicum.playlistmaker.domain.models.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var searchHistoryAdapter: TrackListAdapter
    private var searchRequest = ""
    private var trackList = mutableListOf<Track>()
/*    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()*/
 /*   private val iTunesService = retrofit.create(ITunesSearchApi::class.java)*/
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { startSearch() }
    private var isClickAllowed = true
    private val getSearchHistoryListUseCase by lazy { Creator.provideGetSearchHistoryListUseCase() }
    private val saveSearchHistoryUseCase by lazy { Creator.provideSaveSearchHistoryUseCase() }
    private val clearSearchHistoryUseCase by lazy { Creator.provideClearSearchHistoryUseCase() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null)
            binding.inputEditText.setText(savedInstanceState.getString(SEARCH_REQUEST, ""))

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        /**
         * Получение данных и параметры отображения истории поиска
         * Реализация отклика на нажатие элемента списка истории поиска
         */
        searchHistoryAdapter = TrackListAdapter { track ->
            if (clickDebounce()) goToPlayer(track)
        }.apply {
            trackList = getSearchHistoryListUseCase.execute()
        }
        binding.searchHistoryRV.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = searchHistoryAdapter
        }

        /**
         * Параметры отображения результатов поиска
         * Реализация отклика на нажатие элемента списка результатов поиска
         * и обновлениение истории поиска
         */
        trackListAdapter = TrackListAdapter { track ->
            if (clickDebounce()) {
                addTrack(track, searchHistoryAdapter)
                goToPlayer(track)
            }
        }.apply {
            trackList = this@SearchActivity.trackList
        }

        binding.tracklistRV.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = trackListAdapter
        }

        /**
         * Реализация взаимодействия с полем ввода запроса поиска
         * Вызов отображения истории поиска
         * Автоматическая активация поиска
         */
        binding.inputEditText.run {
            setOnFocusChangeListener { _, hasFocus ->
                binding.searchHistoryLayout.isVisible =
                    searchHistoryVisibility(hasFocus, binding.inputEditText.text)
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideAllViews()
                    startSearch()
                    true
                }
                false
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // empty
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    with(binding) {
                        clearButton.isVisible = clearButtonVisibility(s)
                        searchHistoryLayout.isVisible =
                            searchHistoryVisibility(inputEditText.hasFocus(), s)
                        searchHistoryRV.smoothScrollToPosition(0)
                        tracklistRV.isVisible = false
                        searchDebounce()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    searchRequest = s.toString()
                }
            })
        }

        binding.updateButton.setOnClickListener {
            hideAllViews()
            startSearch()
        }

        binding.clearButton.setOnClickListener {
            binding.inputEditText.setText("")
            binding.inputEditText.clearFocus()
            hideAllViews()
            hideKeyboard(binding.inputEditText)
        }

        binding.clearHistoryButton.setOnClickListener {
            clearSearchHistoryUseCase.execute()
            searchHistoryAdapter.trackList.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            binding.searchHistoryLayout.isVisible = false
        }
    }

    private fun addTrack(track: Track, adapter: TrackListAdapter) {
        if (adapter.trackList.contains(track)) {
            val index = adapter.trackList.indexOf(track)
            adapter.trackList.remove(track)
            adapter.notifyItemRemoved(index)
            adapter.notifyItemRangeChanged(index, adapter.trackList.size - 1)
        }

        adapter.trackList.add(0, track)
        adapter.notifyItemInserted(0)
        if (adapter.trackList.size == MAX_LIST_SIZE + 1) {
            adapter.trackList.remove(adapter.trackList[MAX_LIST_SIZE])
            adapter.notifyItemRemoved(10)
        }
    }

    private fun goToPlayer(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(TRACK_TO_PLAY, track)
        }
        startActivity(intent)
    }

    private fun startSearch() {
        with(binding) {
            if (inputEditText.text.isNotEmpty()) {
                progressBar.isVisible = true
                Creator.provudeSearchTracksUseCase()
                    .execute(inputEditText.text.toString(), object : SearchTracksResultConsumer {
                        override fun consume(result: ConvertedResponse) {

                            trackList.clear()

                            val runnable = {
                                progressBar.isVisible = false
                                when (result.state) {
                                SearchState.FAILURE -> showOnFailure()
                                SearchState.EMPTY -> showEmptyResult()
                                SearchState.SUCCESS -> showSearchResult(result)
                            }

                        }
                            Handler(Looper.getMainLooper()).post(runnable)
                        }
                    })
            }
        }
    }

    private fun ActivitySearchBinding.showSearchResult(result: ConvertedResponse) {
        trackList.addAll(result.results!!)
        tracklistRV.adapter?.notifyDataSetChanged()
        tracklistRV.isVisible = true
    }

    private fun showEmptyResult() {
        with(binding) {
            tracklistRV.isVisible = false
            placeholderImage.isVisible = true
            placeholderImage.setImageResource(R.drawable.search_failed)
            placeholderMessage.isVisible = true
            placeholderMessage.setText(R.string.search_failed)
        }
    }

    private fun showOnFailure() {
        with(binding) {
            tracklistRV.isVisible = false
            placeholderImage.isVisible = true
            placeholderImage.setImageResource(R.drawable.no_internet)
            placeholderMessage.isVisible = true
            placeholderMessage.setText(R.string.no_internet)
            updateButton.isVisible = true
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(searchRunnable)
    }

    override fun onStop() {
        saveSearchHistoryUseCase.execute(searchHistoryAdapter.trackList)
        super.onStop()
    }

    private fun hideAllViews() {
        with(binding) {
            updateButton.isVisible = false
            placeholderImage.isVisible = false
            placeholderMessage.isVisible = false
            tracklistRV.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchRequest)
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    private fun searchHistoryVisibility(hasFocus: Boolean, s: CharSequence?): Boolean {
        val visibility =
            (hasFocus && s?.isEmpty() == true && searchHistoryAdapter.trackList.size > 0)
        if (visibility) hideAllViews()
        return visibility
    }

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    companion object {
        private const val SEARCH_REQUEST = "SEARCH_REQUEST"
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}

