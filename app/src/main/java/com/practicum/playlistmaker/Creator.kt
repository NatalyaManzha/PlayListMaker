package com.practicum.playlistmaker



import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.shared_preferenses.AppThemeRepositoryImpl
import com.practicum.playlistmaker.player.data.MediaPlayerControllerImpl
import com.practicum.playlistmaker.data.network.api.NetworkClient
import com.practicum.playlistmaker.data.network.impl.NetworkClientImpl
import com.practicum.playlistmaker.data.shared_preferenses.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.network.impl.SearchTrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.AppThemeRepository
import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.domain.api.useCase.CheckoutSavedAppThemeUseCase
import com.practicum.playlistmaker.domain.api.useCase.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.player.domain.api.ControlMediaPlayerUseCase
import com.practicum.playlistmaker.domain.api.useCase.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.domain.api.useCase.SaveThemeUseCase
import com.practicum.playlistmaker.domain.api.useCase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.impl.useCase.app_theme.CheckoutSavedAppThemeUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.app_theme.SaveThemeUseCaseImpl
import com.practicum.playlistmaker.player.domain.impl.ControlMediaPlayerUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.search_history.ClearSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.search_history.GetSearchHistoryListUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.search_history.SaveSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.useCase.search_tracks.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.player.data.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.ChangeFavoriteTracksUseCase
import com.practicum.playlistmaker.player.domain.api.CheckFavoriteTracksUseCase
import com.practicum.playlistmaker.player.domain.api.FavoriteTracksRepository
import com.practicum.playlistmaker.player.domain.impl.ChangeFavoriteTracksUseCaseImpl
import com.practicum.playlistmaker.player.domain.impl.CheckFavoriteTracksUseCaseImpl

object Creator {

    private lateinit var application: Application
    private const val PREFERENCES = "preferences"


    fun setApplication(application: Application) {
        this.application = application
    }

    fun provudeSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCaseImpl(getTrackRepository())
    }

    fun provideControlMediaPlayerUseCase(): ControlMediaPlayerUseCase {
        return ControlMediaPlayerUseCaseImpl(getMediaPlayerControl())
    }

    fun provideChangeFavoriteTracksUseCase(): ChangeFavoriteTracksUseCase {
        return ChangeFavoriteTracksUseCaseImpl(getFavoriteTracksRepository())
    }

    fun provideCheckFavoriteTracksUseCase() : CheckFavoriteTracksUseCase {
        return CheckFavoriteTracksUseCaseImpl(getFavoriteTracksRepository())
    }

    fun provideSaveThemeUseCase(): SaveThemeUseCase {
        return SaveThemeUseCaseImpl(getAppThemeRepository())
    }

    fun provideCheckoutSavedAppThemeUseCase(): CheckoutSavedAppThemeUseCase {
        return CheckoutSavedAppThemeUseCaseImpl(getAppThemeRepository())
    }

    fun provideGetSearchHistoryListUseCase(): GetSearchHistoryListUseCase {
        return GetSearchHistoryListUseCaseImpl(getSearchHistoryRepository())
    }

    fun provideSaveSearchHistoryUseCase(): com.practicum.playlistmaker.domain.api.useCase.SaveSearchHistoryUseCase {
        return SaveSearchHistoryUseCaseImpl(getSearchHistoryRepository())
    }

    fun provideClearSearchHistoryUseCase(): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCaseImpl(getSearchHistoryRepository())
    }

    private fun getTrackRepository(): SearchTrackRepository {
        return SearchTrackRepositoryImpl(getNetworcClient())
    }

    private fun getNetworcClient(): NetworkClient {
        return NetworkClientImpl()
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(getSharedPreferences())
    }

    private fun getAppThemeRepository(): AppThemeRepository {
        return AppThemeRepositoryImpl(getSharedPreferences())
    }

    private fun getMediaPlayerControl(): MediaPlayerController {
        return MediaPlayerControllerImpl()
    }

    private fun getSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    private fun getFavoriteTracksRepository(): FavoriteTracksRepository {
        return FavoriteTracksRepositoryImpl(getSharedPreferences())
    }
}