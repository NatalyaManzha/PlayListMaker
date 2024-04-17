package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryListUseCase
import com.practicum.playlistmaker.search.domain.api.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.impl.ClearSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.GetSearchHistoryListUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.SaveSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.settings.domain.api.CheckoutSavedAppThemeUseCase
import com.practicum.playlistmaker.settings.domain.api.SaveThemeUseCase
import com.practicum.playlistmaker.settings.domain.impl.CheckoutSavedAppThemeUseCaseImpl
import com.practicum.playlistmaker.settings.domain.impl.SaveThemeUseCaseImpl
import com.practicum.playlistmaker.sharing.data.impl.ShareLinkUseCaseImpl
import com.practicum.playlistmaker.sharing.data.impl.UserAgreementUseCaseImpl
import com.practicum.playlistmaker.sharing.data.impl.WriteToSupportUseCaseImpl
import com.practicum.playlistmaker.sharing.domain.api.ShareLinkUseCase
import com.practicum.playlistmaker.sharing.domain.api.UserAgreementUseCase
import com.practicum.playlistmaker.sharing.domain.api.WriteToSupportUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single<ClearSearchHistoryUseCase> {
        ClearSearchHistoryUseCaseImpl(get())
    }

    single<SaveSearchHistoryUseCase> {
        SaveSearchHistoryUseCaseImpl(get())
    }

    single<GetSearchHistoryListUseCase> {
        GetSearchHistoryListUseCaseImpl(get())
    }

    single<CheckoutSavedAppThemeUseCase> {
        CheckoutSavedAppThemeUseCaseImpl(get())
    }

    single<SaveThemeUseCase> {
        SaveThemeUseCaseImpl(get())
    }

    single<SearchTracksUseCase> {
        SearchTracksUseCaseImpl(get())
    }

    single<UserAgreementUseCase> {
        UserAgreementUseCaseImpl(get(), get())
    }

    single<WriteToSupportUseCase> {
        WriteToSupportUseCaseImpl(get(), get())
    }

    single<ShareLinkUseCase> {
        ShareLinkUseCaseImpl(get(), get())
    }
}