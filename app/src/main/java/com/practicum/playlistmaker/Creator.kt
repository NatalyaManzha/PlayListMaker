package com.practicum.playlistmaker


import android.app.Application
import com.practicum.playlistmaker.data.impl.AppThemeRepositoryImpl
import com.practicum.playlistmaker.data.impl.MediaPlayerControlImpl
import com.practicum.playlistmaker.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.domain.api.AppThemeRepository
import com.practicum.playlistmaker.domain.api.MediaPlayerControl
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.useCase.CheckoutSavedAppTheme
import com.practicum.playlistmaker.domain.api.useCase.ClearSearchHistory
import com.practicum.playlistmaker.domain.api.useCase.ControlMediaPlayer
import com.practicum.playlistmaker.domain.api.useCase.GetSearchHistoryList
import com.practicum.playlistmaker.domain.api.useCase.SaveSearchHistory
import com.practicum.playlistmaker.domain.api.useCase.SaveTheme
import com.practicum.playlistmaker.domain.impl.useCase.app_theme.CheckoutSavedAppThemeUseCase
import com.practicum.playlistmaker.domain.impl.useCase.app_theme.SaveThemeUseCase
import com.practicum.playlistmaker.domain.impl.useCase.media_player.ControlMediaPlayerUseCase
import com.practicum.playlistmaker.domain.impl.useCase.search_history.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.impl.useCase.search_history.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.domain.impl.useCase.search_history.SaveSearchHistoryUseCase

object Creator {

    private lateinit var application: Application


    fun setApplication(application: Application) {
        this.application = application
    }

    fun provideControlMediaPlayerUseCase(): ControlMediaPlayer {
        return ControlMediaPlayerUseCase(provideMediaPlayerControl())
    }

    fun provideSaveThemeUseCase(): SaveTheme {
        return SaveThemeUseCase(provideAppThemeRepository())
    }

    fun provideCheckoutSavedAppThemeUseCase(): CheckoutSavedAppTheme {
        return CheckoutSavedAppThemeUseCase(provideAppThemeRepository())
    }

    fun provideGetSearchHistoryListUseCase(): GetSearchHistoryList {
        return GetSearchHistoryListUseCase(provideSearchHistiryRepository())
    }

    fun provideSaveSearchHistoryUseCase(): SaveSearchHistory {
        return SaveSearchHistoryUseCase(provideSearchHistiryRepository())
    }

    fun provideClearSearchHistoryUseCase(): ClearSearchHistory {
        return ClearSearchHistoryUseCase(provideSearchHistiryRepository())
    }

    private fun provideSearchHistiryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(application)
    }

    private fun provideAppThemeRepository(): AppThemeRepository {
        return AppThemeRepositoryImpl(application)
    }

    private fun provideMediaPlayerControl(): MediaPlayerControl {
        return MediaPlayerControlImpl()
    }
}