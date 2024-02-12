package com.practicum.playlistmaker


import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.search.data.network.api.NetworkClient
import com.practicum.playlistmaker.search.data.network.impl.NetworkClientImpl
import com.practicum.playlistmaker.search.data.network.impl.SearchTrackRepositoryImpl
import com.practicum.playlistmaker.settings.data.impl.AppThemeRepositoryImpl
import com.practicum.playlistmaker.search.data.local.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.AppThemeRepository
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.SearchTrackRepository
import com.practicum.playlistmaker.settings.domain.api.CheckoutSavedAppThemeUseCase
import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.search.domain.api.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.settings.domain.api.SaveThemeUseCase
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.settings.domain.impl.CheckoutSavedAppThemeUseCaseImpl
import com.practicum.playlistmaker.settings.domain.impl.SaveThemeUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.ClearSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.GetSearchHistoryListUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.SaveSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.player.data.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.player.data.MediaPlayerControllerImpl
import com.practicum.playlistmaker.player.domain.api.ChangeFavoriteTracksUseCase
import com.practicum.playlistmaker.player.domain.api.CheckFavoriteTracksUseCase
import com.practicum.playlistmaker.player.domain.api.ControlMediaPlayerUseCase
import com.practicum.playlistmaker.player.domain.api.FavoriteTracksRepository
import com.practicum.playlistmaker.player.domain.api.MediaPlayerController
import com.practicum.playlistmaker.player.domain.impl.ChangeFavoriteTracksUseCaseImpl
import com.practicum.playlistmaker.player.domain.impl.CheckFavoriteTracksUseCaseImpl
import com.practicum.playlistmaker.player.domain.impl.ControlMediaPlayerUseCaseImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.data.impl.ShareLinkUseCaseImpl
import com.practicum.playlistmaker.sharing.data.impl.UserAgreementUseCaseImpl
import com.practicum.playlistmaker.sharing.data.impl.WriteToSupportUseCaseImpl
import com.practicum.playlistmaker.sharing.domain.api.ShareLinkUseCase
import com.practicum.playlistmaker.sharing.domain.api.UserAgreementUseCase
import com.practicum.playlistmaker.sharing.domain.api.WriteToSupportUseCase

object Creator {

    private lateinit var application: Application
    private const val PREFERENCES = "preferences"


    fun setApplication(application: Application) {
        this.application = application
    }
    fun provideShareLinkUseCase(): ShareLinkUseCase {
        return ShareLinkUseCaseImpl(getExternalNavigator())
    }
    fun  provideWriteToSupportUseCase() : WriteToSupportUseCase {
        return WriteToSupportUseCaseImpl(getExternalNavigator())
    }
    fun provideUserAgreementUseCase() : UserAgreementUseCase {
        return UserAgreementUseCaseImpl(getExternalNavigator())
    }

    fun provideSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCaseImpl(getTrackRepository())
    }

    fun provideControlMediaPlayerUseCase(): ControlMediaPlayerUseCase {
        return ControlMediaPlayerUseCaseImpl(getMediaPlayerControl())
    }

    fun provideChangeFavoriteTracksUseCase(): ChangeFavoriteTracksUseCase {
        return ChangeFavoriteTracksUseCaseImpl(getFavoriteTracksRepository())
    }

    fun provideCheckFavoriteTracksUseCase(): CheckFavoriteTracksUseCase {
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

    fun provideSaveSearchHistoryUseCase(): SaveSearchHistoryUseCase {
        return SaveSearchHistoryUseCaseImpl(getSearchHistoryRepository())
    }

    fun provideClearSearchHistoryUseCase(): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCaseImpl(getSearchHistoryRepository())
    }

    private fun getTrackRepository(): SearchTrackRepository {
        return SearchTrackRepositoryImpl(getNetworcClient())
    }
    private fun getExternalNavigator() : ExternalNavigator {
        return ExternalNavigatorImpl(application)
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