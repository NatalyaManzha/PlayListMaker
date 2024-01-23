package com.practicum.playlistmaker


import android.content.Context
import com.practicum.playlistmaker.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.useCase.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.useCase.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.domain.useCase.SaveSearchHistoryUseCase

object Creator {

    fun provideGetSearchHistoryListUseCase(context: Context):GetSearchHistoryListUseCase{
        return GetSearchHistoryListUseCase(provideSearchHistiryRepository(context))
    }

    fun provideSaveSearchHistoryUseCase(context: Context) :SaveSearchHistoryUseCase{
        return SaveSearchHistoryUseCase(provideSearchHistiryRepository(context))
    }

    fun provideClearSearchHistoryUseCase(context: Context): ClearSearchHistoryUseCase{
        return ClearSearchHistoryUseCase(provideSearchHistiryRepository(context))
    }

    private fun provideSearchHistiryRepository(context: Context): SearchHistoryRepository{
        return SearchHistoryRepositoryImpl(context)
    }
}