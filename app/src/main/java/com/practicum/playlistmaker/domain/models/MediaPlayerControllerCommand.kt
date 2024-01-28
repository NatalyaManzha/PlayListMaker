package com.practicum.playlistmaker.domain.models

class MediaPlayerControllerCommand(
    val command: MediaPlayerCommand,
    val url: String?
)