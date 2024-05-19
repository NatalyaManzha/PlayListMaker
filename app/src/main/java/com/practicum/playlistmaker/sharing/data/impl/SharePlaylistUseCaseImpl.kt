package com.practicum.playlistmaker.sharing.data.impl

import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharePlaylistUseCase

class SharePlaylistUseCaseImpl(
    private val externalNavigator:ExternalNavigator
): SharePlaylistUseCase {
    override fun execute(playlistInfo: String) {
        externalNavigator.shareData(playlistInfo)
    }
}