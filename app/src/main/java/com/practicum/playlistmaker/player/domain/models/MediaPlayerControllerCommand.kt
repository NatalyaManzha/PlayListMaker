package com.practicum.playlistmaker.player.domain.models

class MediaPlayerControllerCommand(
    val command: MediaPlayerCommand,
    val url: String?
)