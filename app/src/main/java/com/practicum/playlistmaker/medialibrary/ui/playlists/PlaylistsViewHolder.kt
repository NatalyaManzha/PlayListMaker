package com.practicum.playlistmaker.medialibrary.ui.playlists

import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistPreviewElementBinding
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview

class PlaylistsViewHolder(private val binding: PlaylistPreviewElementBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(playlist: PlaylistPreview) {
        with(binding) {
            playlistName.text = playlist.name
            playlistCount.text = playlist.count
            if (playlist.iconUri == null)
                playlistIcon.setImageResource(R.drawable.placeholder)
            else playlistIcon.setImageURI(playlist.iconUri)
        }
    }
}