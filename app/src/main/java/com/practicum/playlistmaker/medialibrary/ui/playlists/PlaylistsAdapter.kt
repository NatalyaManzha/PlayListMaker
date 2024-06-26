package com.practicum.playlistmaker.medialibrary.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistPreviewElementBinding
import com.practicum.playlistmaker.medialibrary.domain.models.PlaylistPreview

class PlaylistsAdapter(
    private var onItemClickListener: ((PlaylistPreview) -> Unit)
) : RecyclerView.Adapter<PlaylistsViewHolder>() {

    lateinit var playlists: List<PlaylistPreview>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsViewHolder(
            PlaylistPreviewElementBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { _ ->
            onItemClickListener.invoke(playlists[holder.adapterPosition])
        }
    }

    override fun getItemCount() = playlists.size
}