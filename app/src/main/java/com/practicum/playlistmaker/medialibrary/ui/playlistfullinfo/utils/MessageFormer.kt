package com.practicum.playlistmaker.medialibrary.ui.playlistfullinfo.utils

import com.practicum.playlistmaker.player.domain.models.Track

fun formMessage(
    playlistInfo: List<String>,
    tracklist: List<Track>
): String {
    val lines = mutableListOf<String>()
    lines.addAll(playlistInfo)
    tracklist.forEachIndexed { index, track ->
        lines.add("${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTimeMinSec})")
    }
    return lines.joinToString("\n")
}