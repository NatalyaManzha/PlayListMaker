package com.practicum.playlistmaker.search.data.network.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.search.data.network.api.ConnectivityCheck

class ConnectivityCheckImpl(
    private val context: Context
) : ConnectivityCheck {
    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true //передача данных по сети мобильного оператора
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true // по Wi-Fi-соединению
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true //есть активное проводное подключение
            }
        }
        return false
    }
}