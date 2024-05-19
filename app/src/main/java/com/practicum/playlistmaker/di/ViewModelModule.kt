package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.ui.favorites.FavoritesViewModel
import com.practicum.playlistmaker.medialibrary.ui.newplaylist.NewPlaylistViewModel
import com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo.PlaylistFIViewModel
import com.practicum.playlistmaker.medialibrary.ui.playlists.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        PlaylistFIViewModel(get(), get(), get(), get())
    }

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
        PlayerViewModel(get(), get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }
}