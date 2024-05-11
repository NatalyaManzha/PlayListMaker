package com.practicum.playlistmaker.medialibrary.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistElementBinding
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview

class PlaylistsViewHolder(private val binding: PlaylistElementBinding) :
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