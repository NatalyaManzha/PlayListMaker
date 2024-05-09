package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.BuildConfig
import com.practicum.playlistmaker.medialibrary.data.db.FavoritesDatabase
import com.practicum.playlistmaker.medialibrary.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.player.data.MediaPlayerControllerImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.search.data.network.api.ConnectivityCheck
import com.practicum.playlistmaker.search.data.network.api.ITunesSearchApi
import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.impl.ConnectivityCheckImpl
import com.practicum.playlistmaker.search.data.network.impl.NetworkClientImpl
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.utils.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            PlaylistsDatabase::class.java,
            BuildConfig.PLAYLISTS_DB
        ).build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            FavoritesDatabase::class.java,
            BuildConfig.FAVORITES_DB
        ).build()
    }

    single {
        androidContext().getSharedPreferences(BuildConfig.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        NetworkClientImpl(get(), get())
    }

    single<ConnectivityCheck> {
        ConnectivityCheckImpl(androidContext())
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
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApi::class.java)
    }

    single {
        ResourceProvider(androidContext())
    }
}

