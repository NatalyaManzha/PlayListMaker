package com.practicum.playlistmaker


import android.app.Application
import com.practicum.playlistmaker.data.shared_preferenses.AppThemeRepositoryImpl
import com.practicum.playlistmaker.data.media_player.MediaPlayerControlImpl
import com.practicum.playlistmaker.data.network.api.NetworkClient
import com.practicum.playlistmaker.data.network.impl.NetworkClientImpl
import com.practicum.playlistmaker.data.shared_preferenses.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.network.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.AppThemeRepository
import com.practicum.playlistmaker.domain.api.MediaPlayerControl
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.useCase.CheckoutSavedAppThemeUseCase
import com.practicum.playlistmaker.domain.api.useCase.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.useCase.ControlMediaPlayerUseCase
import com.practicum.playlistmaker.domain.api.useCase.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.domain.api.useCase.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.useCase.SaveThemeUseCase
import com.practicum.playlistmaker.domain.api.useCase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.impl.useCase.app_theme.CheckoutSavedAppThemeUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.app_theme.SaveThemeUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.media_player.ControlMediaPlayerUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.search_history.ClearSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.search_history.GetSearchHistoryListUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.search_tracks.SearchTracksUseCaseImpl

object Creator {

    private lateinit var application: Application


    fun setApplication(application: Application) {
        this.application = application
    }

    fun provudeSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCaseImpl(provideTrackRepository())
    }
    fun provideControlMediaPlayerUseCase(): ControlMediaPlayerUseCase {
        return ControlMediaPlayerUseCaseImpl(provideMediaPlayerControl())
    }

    fun provideSaveThemeUseCase(): SaveThemeUseCase {
        return SaveThemeUseCaseImpl(provideAppThemeRepository())
    }

    fun provideCheckoutSavedAppThemeUseCase(): CheckoutSavedAppThemeUseCase {
        return CheckoutSavedAppThemeUseCaseImpl(provideAppThemeRepository())
    }

    fun provideGetSearchHistoryListUseCase(): GetSearchHistoryListUseCase {
        return GetSearchHistoryListUseCaseImpl(provideSearchHistiryRepository())
    }

    fun provideSaveSearchHistoryUseCase(): com.practicum.playlistmaker.domain.api.useCase.SaveSearchHistoryUseCase {
        return SaveSearchHistoryUseCase(provideSearchHistiryRepository())
    }

    fun provideClearSearchHistoryUseCase(): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCaseImpl(provideSearchHistiryRepository())
    }

    private fun provideTrackRepository(): TrackRepository{
        return TrackRepositoryImpl(provideNetworcClient())
    }

    private fun provideNetworcClient(): NetworkClient {
        return NetworkClientImpl()
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