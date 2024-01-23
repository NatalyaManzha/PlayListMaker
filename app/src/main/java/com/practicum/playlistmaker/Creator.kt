package com.practicum.playlistmaker


import android.app.Application
import com.practicum.playlistmaker.data.impl.AppThemeRepositoryImpl
import com.practicum.playlistmaker.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.domain.api.AppThemeRepository
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.useCase.app_theme.CheckoutSavedAppThemeUseCase
import com.practicum.playlistmaker.domain.useCase.app_theme.SaveThemeUseCase
import com.practicum.playlistmaker.domain.useCase.search_history.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.useCase.search_history.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.domain.useCase.search_history.SaveSearchHistoryUseCase

object Creator {

    private lateinit var application: Application

    fun setApplication (application: Application){
        this.application = application
    }

    fun provideSaveThemeUseCase():SaveThemeUseCase{
        return SaveThemeUseCase(provideAppThemeRepository())
    }
    fun provideCheckoutSavedAppThemeUseCase():CheckoutSavedAppThemeUseCase{
        return CheckoutSavedAppThemeUseCase(provideAppThemeRepository())
    }

    fun provideGetSearchHistoryListUseCase(): GetSearchHistoryListUseCase {
        return GetSearchHistoryListUseCase(provideSearchHistiryRepository())
    }

    fun provideSaveSearchHistoryUseCase() : SaveSearchHistoryUseCase {
        return SaveSearchHistoryUseCase(provideSearchHistiryRepository())
    }

    fun provideClearSearchHistoryUseCase(): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCase(provideSearchHistiryRepository())
    }

    private fun provideSearchHistiryRepository(): SearchHistoryRepository{
        return SearchHistoryRepositoryImpl(application)
    }
    private fun provideAppThemeRepository() : AppThemeRepository{
        return AppThemeRepositoryImpl(application)
    }
}