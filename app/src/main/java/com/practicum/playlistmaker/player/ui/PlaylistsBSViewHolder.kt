package com.practicum.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistElementBinding
import com.practicum.playlistmaker.databinding.PlaylistPreviewElementBinding
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview

class PlaylistsBSViewHolder(private val binding: PlaylistElementBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(playlist: PlaylistPreview) {
        with(binding) {
            elementPlaylistName.text = playlist.name
            elementPlaylistCount.text = playlist.count
            if (playlist.iconUri == null)
                elementPlaylistIcon.setImageResource(R.drawable.placeholder)
            else elementPlaylistIcon.setImageURI(playlist.iconUri)
        }
    }
}