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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var searchRequest: String

    private lateinit var toolbar: Toolbar
    private lateinit var tracklistRecyclerView: RecyclerView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var updateButton: Button
    private var adapter = TrackListAdapter()
    private var trackList = ArrayList<Track>()
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    companion object {
        private const val SEARCH_REQUEST = "SEARCH_REQUEST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        placeholderImage = findViewById(R.id.search_status_icon)
        placeholderMessage = findViewById(R.id.search_failed_message)
        updateButton = findViewById(R.id.update_button)
        tracklistRecyclerView = findViewById<RecyclerView>(R.id.tracklist_rv)
        inputEditText = findViewById<EditText>(R.id.search_input)
        clearButton = findViewById<ImageView>(R.id.clearButton)

        if (savedInstanceState != null)
            inputEditText.setText(savedInstanceState.getString(SEARCH_REQUEST, ""))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        adapter.trackList = trackList
        tracklistRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracklistRecyclerView.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSearch()
                true
            }
            false
        }

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

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }
        inputEditText.addTextChangedListener(textWatcher)
    }

    private fun turnOffAllViews() {
        updateButton.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        tracklistRecyclerView.visibility = View.GONE
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
                            adapter.notifyDataSetChanged()
                            tracklistRecyclerView.visibility = View.VISIBLE
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
        tracklistRecyclerView.visibility = View.GONE
        placeholderImage.visibility = View.VISIBLE
        placeholderImage.setImageResource(R.drawable.no_internet)
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.setText(R.string.no_internet)
        updateButton.visibility = View.VISIBLE
    }

    private fun showEmptyResult() {
        tracklistRecyclerView.visibility = View.GONE
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
}