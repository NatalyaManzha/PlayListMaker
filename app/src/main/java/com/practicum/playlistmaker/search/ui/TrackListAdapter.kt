package com.practicum.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TracklistElementBinding
import com.practicum.playlistmaker.player.domain.models.Track

class TrackListAdapter(
    private var onItemClickListener: ((Track) -> Unit)
) : RecyclerView.Adapter<TrackListViewHolder>() {

    lateinit var trackList: List<Track>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackListViewHolder(TracklistElementBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { _ ->
            onItemClickListener.invoke(trackList[holder.adapterPosition])
        }
    }

    override fun getItemCount() = trackList.size
}