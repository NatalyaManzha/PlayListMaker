package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.network.api.ITunesSearchApi
import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.dto.Response
import com.practicum.playlistmaker.search.data.network.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImpl : NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
    override fun doRequest(request: TrackSearchRequest): Response {
        return try {
            val response = iTunesService.search(request.expression).execute()
            val networkResponse = response.body() ?: Response()
            networkResponse.apply { stateCode = response.code() }
        } catch (ex: Exception) {
            Response().apply { stateCode = 400 }
        }
    }

    companion object {
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}