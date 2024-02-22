package com.practicum.playlistmaker.sharing.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.WriteToSupportUseCase
import com.practicum.playlistmaker.utils.ResourceProvider

class WriteToSupportUseCaseImpl(
    private var externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider
) : WriteToSupportUseCase {
    override fun execute() {
        externalNavigator.writeToSupport(
            subject = resourceProvider.getString(R.string.support_message_subject),
            message = resourceProvider.getString(R.string.thanks),
            email = resourceProvider.getString(R.string.email)
        )
    }
}