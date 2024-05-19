package com.example.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.createplaylist.models.Playlist

class PlaylistsLinearLayoutAdapter(
    var playlists: ArrayList<Playlist>,
    private val clickListener: OnPlaylistClickListener
) : RecyclerView.Adapter<PlaylistsLinearLayoutViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsLinearLayoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item_linear, parent, false)
        return PlaylistsLinearLayoutViewHolder(view)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistsLinearLayoutViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onPlaylistClick(playlists[position]) }
    }


    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }


}