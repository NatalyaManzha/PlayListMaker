package com.practicum.playlistmaker.sharing.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.utils.ResourceProvider
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.UserAgreementUseCase

class UserAgreementUseCaseImpl(private val externalNavigator: ExternalNavigator) :
    UserAgreementUseCase {
    override fun execute() {
        val url = ResourceProvider.getString(R.string.offer_link)
        externalNavigator.openUserAgreement(url)
    }
}