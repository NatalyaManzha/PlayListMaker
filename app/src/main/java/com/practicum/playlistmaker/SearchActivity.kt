package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    private lateinit var trackListAdapter: ResultTrackListAdapter
    private var searchRequest = ""
    private var searchHistoryAdapter = TrackListAdapter()
    private var trackList = ArrayList<Track>()
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

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

        val sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        /**
         * Получение данных и параметры отображения истории поиска
         */
        searchHistoryAdapter.trackList = searchHistory.getSearchList()
        searchHistoryRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistoryRV.adapter = searchHistoryAdapter

        /**
         * Реализация отклика на нажатие элемента списка результатов поиска
         * и обновлениение истории поиска
         */
        val onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(track: Track) {
                searchHistory.addTrack(track)
            }
        }

        var listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                var track: Track
                if (key == SEARCH_HISTORY_UPDATE) {
                    val json = sharedPreferences?.getString(key, null)
                    if (json != null) {
                        track = Gson().fromJson(json, Track::class.java)
                        searchHistoryAdapter.trackList.forEachIndexed() { index, element ->
                            val x = index
                            if (element.trackId == track.trackId) {
                                searchHistoryAdapter.trackList.remove(element)
                                searchHistoryAdapter.notifyItemRemoved(x)
                            }
                        }
                        searchHistoryAdapter.trackList.add(0, track)
                        searchHistoryAdapter.notifyItemInserted(0)
                        if (searchHistoryAdapter.trackList.size == 11) {
                            searchHistoryAdapter.trackList.remove(
                                searchHistoryAdapter.trackList[10]
                            )
                            searchHistoryAdapter.notifyItemRemoved(10)
                        }
                    }
                }
            }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        /**
         * Параметры отображения результатов поиска
         */
        trackListAdapter = ResultTrackListAdapter(onItemClickListener)
        trackListAdapter.trackList = trackList
        tracklistRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracklistRV.adapter = trackListAdapter

        /**
         * Реализация взаимодействия с полем ввода запроса поиска
         * Вызов отображения истории поиска
         */
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryLayout.visibility = searchHistoryVisibility(hasFocus, inputEditText.text)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSearch()
                true
            }
            false
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchHistoryLayout.visibility = searchHistoryVisibility(inputEditText.hasFocus(), s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }

        inputEditText.addTextChangedListener(textWatcher)


        updateButton.setOnClickListener {
            turnOffAllViews()
            startSearch()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            turnOffAllViews()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearSearchHistory(searchHistoryAdapter.trackList)
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryLayout.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveSearchHistory(searchHistoryAdapter.trackList)
    }

    private fun turnOffAllViews() {
        updateButton.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        tracklistRV.visibility = View.GONE
    }

    private fun startSearch() {
        turnOffAllViews()
        if (inputEditText.text.isNotEmpty()) {
            iTunesService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            trackListAdapter.notifyDataSetChanged()
                            tracklistRV.visibility = View.VISIBLE
                        }
                        if (trackList.isEmpty()) {
                            showEmptyResult()
                        }
                    } else {
                        showOnFailure()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showOnFailure()
                }
            })
        }
    }

    private fun showOnFailure() {
        tracklistRV.visibility = View.GONE
        placeholderImage.visibility = View.VISIBLE
        placeholderImage.setImageResource(R.drawable.no_internet)
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.setText(R.string.no_internet)
        updateButton.visibility = View.VISIBLE
    }

    private fun showEmptyResult() {
        tracklistRV.visibility = View.GONE
        placeholderImage.visibility = View.VISIBLE
        placeholderImage.setImageResource(R.drawable.search_failed)
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.setText(R.string.search_failed)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchRequest)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchHistoryVisibility (hasFocus : Boolean, s: CharSequence?) : Int {
        Log.d("Видимость", "Размер памяти ${searchHistoryAdapter.trackList.size}")
        return if (hasFocus && s?.isEmpty() == true && searchHistoryAdapter.trackList.size>0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        private const val SEARCH_REQUEST = "SEARCH_REQUEST"
    }
}