package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.medialibrary.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.medialibrary.domain.impl.FavoritesInteractorImpl
import com.practicum.playlistmaker.medialibrary.domain.impl.PlaylistsInteractorImpl
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}