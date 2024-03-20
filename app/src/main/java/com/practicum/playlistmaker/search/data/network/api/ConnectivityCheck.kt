package com.practicum.playlistmaker.search.data.network.api

interface ConnectivityCheck {
    fun isConnected(): Boolean
}