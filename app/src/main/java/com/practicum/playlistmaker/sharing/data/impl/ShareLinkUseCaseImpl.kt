package com.practicum.playlistmaker.sharing.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.ShareLinkUseCase
import com.practicum.playlistmaker.utils.ResourceProvider

class ShareLinkUseCaseImpl(
    private val externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider
) : ShareLinkUseCase {
    override fun execute() {
        val url = resourceProvider.getString(R.string.android_developer_course_link)
        externalNavigator.shareLink(url)
    }
}
