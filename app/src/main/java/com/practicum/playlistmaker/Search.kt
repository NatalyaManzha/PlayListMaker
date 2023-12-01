package com.practicum.playlistmaker

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Track (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)

class TrackResponse (
    val resultCount: Int,
    val results: List <Track>
    )

interface ITunesSearchApi {
    @GET ("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}

class Search(
    val inputEditText: EditText,
    var trackList: MutableList<Track>,
    val tracklistRV: RecyclerView,
    val placeholderImage: ImageView,
    val placeholderMessage: TextView,
    val updateButton: Button
) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    fun startSearch() {
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
                            tracklistRV.adapter?.notifyDataSetChanged()
                            tracklistRV.isVisible = true
                        }
                        if (trackList.isEmpty()) showEmptyResult()
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
    companion object {
        private val iTunesBaseUrl = "https://itunes.apple.com"
    }
}