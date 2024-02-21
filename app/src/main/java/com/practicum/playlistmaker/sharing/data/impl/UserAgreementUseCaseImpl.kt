package com.practicum.playlistmaker.sharing.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.UserAgreementUseCase
import com.practicum.playlistmaker.utils.ResourceProvider

class UserAgreementUseCaseImpl(
    private val externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider
) :
    UserAgreementUseCase {
    override fun execute() {
        val url = resourceProvider.getString(R.string.offer_link)
        externalNavigator.openUserAgreement(url)
    }
}