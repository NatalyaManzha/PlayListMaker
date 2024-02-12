package com.practicum.playlistmaker.sharing.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ResourceProvider
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.WriteToSupportUseCase

class WriteToSupportUseCaseImpl(private var externalNavigator: ExternalNavigator):
    WriteToSupportUseCase {
      override fun execute() {
        externalNavigator.writeToSupport(
            subject = ResourceProvider.getString(R.string.support_message_subject),
            message = ResourceProvider.getString(R.string.thanks),
            email = ResourceProvider.getString(R.string.email)
        )
    }
}