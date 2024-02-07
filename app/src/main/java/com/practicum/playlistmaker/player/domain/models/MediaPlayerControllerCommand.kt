package com.practicum.playlistmaker.player.domain.models

import com.practicum.playlistmaker.player.domain.models.MediaPlayerCommand

class MediaPlayerControllerCommand(
    val command: MediaPlayerCommand,
    val url: String?
)