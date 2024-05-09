package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.ui.FavoritesViewModel
import com.practicum.playlistmaker.medialibrary.ui.NewPlaylistViewModel
import com.practicum.playlistmaker.medialibrary.ui.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        PlayerViewModel(get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }
}