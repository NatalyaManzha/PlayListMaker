package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.data.impl.FavoritesRepositoryImpl
import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.data.local.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.network.impl.SearchTrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.settings.data.impl.AppThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.AppThemeRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get())
    }

    single<AppThemeRepository> {
        AppThemeRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SearchTrackRepository> {
        SearchTrackRepositoryImpl(get())
    }
}