package com.practicum.playlistmaker.sharing.domain.api

interface SharePlaylistUseCase {
    fun execute(playlistInfo: String)
}