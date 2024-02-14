package com.practicum.playlistmaker.sharing.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.utils.ResourceProvider
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.ShareLinkUseCase

class ShareLinkUseCaseImpl(private val externalNavigator: ExternalNavigator) : ShareLinkUseCase {
    override fun execute() {
        val url = ResourceProvider.getString(R.string.android_developer_course_link)
        externalNavigator.shareLink(url)
    }
}
