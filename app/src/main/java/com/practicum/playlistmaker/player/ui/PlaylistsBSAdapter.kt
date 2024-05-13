package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistElementBinding
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview

class PlaylistsBSAdapter(
    private var onItemClickListener: ((PlaylistPreview) -> Unit)
) : RecyclerView.Adapter<PlaylistsBSViewHolder>() {

    lateinit var playlists: List<PlaylistPreview>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsBSViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsBSViewHolder(
            PlaylistElementBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistsBSViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { _ ->
            onItemClickListener.invoke(playlists[holder.adapterPosition])
        }
    }

    override fun getItemCount() = playlists.size
}