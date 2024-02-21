package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.MediaPlayerControllerImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.search.data.network.api.ITunesSearchApi

import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.impl.NetworkClientImpl
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.utils.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

val dataModule = module {

    single {
        androidContext().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        NetworkClientImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory<MediaPlayerController> {
        MediaPlayerControllerImpl(get())
    }

    factory { MediaPlayer() }

    factory { Gson() }

    single<ITunesSearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApi::class.java)
    }

    single {
        Executors.newCachedThreadPool()
    }

    factory {
        Handler(Looper.getMainLooper())
    }

    single {
        ResourceProvider(androidContext())
    }
}

