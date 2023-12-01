package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {


    private lateinit var toolbar: Toolbar
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
    private lateinit var search: Search
    private var searchRequest = ""
    private var searchHistoryAdapter = TrackListAdapter()
    private var trackList = mutableListOf<Track>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        placeholderImage = findViewById(R.id.search_status_icon)
        placeholderMessage = findViewById(R.id.search_failed_message)
        updateButton = findViewById<Button>(R.id.update_button)
        tracklistRV = findViewById<RecyclerView>(R.id.tracklist_rv)
        inputEditText = findViewById<EditText>(R.id.search_input)
        clearButton = findViewById<ImageView>(R.id.clearButton)
        searchHistoryLayout = findViewById<LinearLayout>(R.id.search_history_layout)
        searchHistoryRV = findViewById<RecyclerView>(R.id.search_history_rv)
        clearHistoryButton = findViewById<Button>(R.id.clear_history_button)

        if (savedInstanceState != null)
            inputEditText.setText(savedInstanceState.getString(SEARCH_REQUEST, ""))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        search = Search(
            inputEditText,
            trackList,
            tracklistRV,
            placeholderImage,
            placeholderMessage,
            updateButton
        )
        searchHistory = SearchHistory(this)

        /**
         * Получение данных и параметры отображения истории поиска
         */
        searchHistoryAdapter.trackList = searchHistory.getSearchList().toMutableList()
        searchHistoryRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistoryRV.adapter = searchHistoryAdapter


        /**
         * Параметры отображения результатов поиска
         * Реализация отклика на нажатие элемента списка результатов поиска
         * и обновлениение истории поиска
         */
        trackListAdapter = TrackListAdapter()
        trackListAdapter.trackList = trackList
        trackListAdapter.onItemClickListener =  { track ->
            searchHistory.addTrack(track, searchHistoryAdapter)
        }
        tracklistRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracklistRV.adapter = trackListAdapter

        /**
         * Реализация взаимодействия с полем ввода запроса поиска
         * Вызов отображения истории поиска
         */
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.isVisible = searchHistoryVisibility(hasFocus, inputEditText.text)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideAllViews()
                search.startSearch()
                true
            }
            false
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = clearButtonVisibility(s)
                searchHistoryLayout.isVisible = searchHistoryVisibility(inputEditText.hasFocus(), s)
                searchHistoryRV.smoothScrollToPosition(0)
                tracklistRV.isVisible = false
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }

        inputEditText.addTextChangedListener(textWatcher)


        updateButton.setOnClickListener {
            hideAllViews()
            search.startSearch()
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

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) false else true
    }

    private fun searchHistoryVisibility(hasFocus: Boolean, s: CharSequence?): Boolean {
        return if (hasFocus && s?.isEmpty() == true && searchHistoryAdapter.trackList.size > 0) true else false
    }

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    companion object {
        private const val SEARCH_REQUEST = "SEARCH_REQUEST"
    }
}

