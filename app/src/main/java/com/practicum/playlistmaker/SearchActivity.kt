package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {


    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var tracklistRV: RecyclerView
    private lateinit var searchHistoryRV: RecyclerView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var updateButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var searchHistory: SearchHistory
    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var searchHistoryAdapter: TrackListAdapter
    private var searchRequest = ""
    private var trackList = mutableListOf<Track>()
    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { startSearch() }
    private var isClickAllowed = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.progress_bar)
        placeholderImage = findViewById(R.id.search_status_icon)
        placeholderMessage = findViewById(R.id.search_failed_message)
        updateButton = findViewById(R.id.update_button)
        tracklistRV = findViewById(R.id.tracklist_rv)
        inputEditText = findViewById(R.id.search_input)
        clearButton = findViewById(R.id.clearButton)
        searchHistoryLayout = findViewById(R.id.search_history_layout)
        searchHistoryRV = findViewById(R.id.search_history_rv)
        clearHistoryButton = findViewById(R.id.clear_history_button)

        if (savedInstanceState != null)
            inputEditText.setText(savedInstanceState.getString(SEARCH_REQUEST, ""))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        searchHistory = SearchHistory(this)

        /**
         * Получение данных и параметры отображения истории поиска
         * Реализация отклика на нажатие элемента списка истории поиска
         */
        searchHistoryAdapter = TrackListAdapter { track ->
            if (clickDebounce()) goToPlayer(track)
        }.apply {
            trackList = searchHistory.getSearchList().toMutableList()
        }
        searchHistoryRV.apply {
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
                searchHistory.addTrack(track, searchHistoryAdapter)
                goToPlayer(track)
            }
        }.apply {
            trackList = this@SearchActivity.trackList
        }

        tracklistRV.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = trackListAdapter
        }

        /**
         * Реализация взаимодействия с полем ввода запроса поиска
         * Вызов отображения истории поиска
         * Автоматическая активация поиска
         */
        inputEditText.run {
            setOnFocusChangeListener { _, hasFocus ->
                searchHistoryLayout.isVisible =
                    searchHistoryVisibility(hasFocus, inputEditText.text)
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
                    clearButton.isVisible = clearButtonVisibility(s)
                    searchHistoryLayout.isVisible =
                        searchHistoryVisibility(inputEditText.hasFocus(), s)
                    searchHistoryRV.smoothScrollToPosition(0)
                    tracklistRV.isVisible = false
                    searchDebounce()
                }

                override fun afterTextChanged(s: Editable?) {
                    searchRequest = s.toString()
                }
            })
        }

        updateButton.setOnClickListener {
            hideAllViews()
            startSearch()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            hideAllViews()
            hideKeyboard(inputEditText)
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearSearchHistory(searchHistoryAdapter.trackList)
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryLayout.isVisible = false
        }
    }

    private fun goToPlayer(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(TRACK_TO_PLAY, track)
        }
        startActivity(intent)
    }

    private fun startSearch() {
        if (inputEditText.text.isNotEmpty()) {
            progressBar.isVisible = true
            iTunesService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    progressBar.isVisible = false
                    if (response.code() == 200) {
                        trackList.clear()
                        val resultList = response.body()?.results
                        if (resultList != null && resultList.isNotEmpty() == true) {
                            trackList.addAll(resultList)
                            tracklistRV.adapter?.notifyDataSetChanged()
                            tracklistRV.isVisible = true
                        }
                        if (trackList.isEmpty()) showEmptyResult()
                    } else {
                        showOnFailure()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    progressBar.isVisible = false
                    showOnFailure()
                }
            })
        }
    }

    private fun showEmptyResult() {
        tracklistRV.isVisible = false
        placeholderImage.isVisible = true
        placeholderImage.setImageResource(R.drawable.search_failed)
        placeholderMessage.isVisible = true
        placeholderMessage.setText(R.string.search_failed)
    }

    private fun showOnFailure() {
        tracklistRV.isVisible = false
        placeholderImage.isVisible = true
        placeholderImage.setImageResource(R.drawable.no_internet)
        placeholderMessage.isVisible = true
        placeholderMessage.setText(R.string.no_internet)
        updateButton.isVisible = true
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(searchRunnable)
    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveSearchHistory(searchHistoryAdapter.trackList)
    }

    private fun hideAllViews() {
        updateButton.isVisible = false
        placeholderImage.isVisible = false
        placeholderMessage.isVisible = false
        tracklistRV.isVisible = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchRequest)
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    private fun searchHistoryVisibility(hasFocus: Boolean, s: CharSequence?): Boolean {
        val visibility = (hasFocus && s?.isEmpty() == true && searchHistoryAdapter.trackList.size > 0)
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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        private const val SEARCH_REQUEST = "SEARCH_REQUEST"
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

