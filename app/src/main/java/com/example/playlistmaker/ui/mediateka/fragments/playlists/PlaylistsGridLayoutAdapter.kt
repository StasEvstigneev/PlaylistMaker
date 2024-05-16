package com.example.playlistmaker.ui.mediateka.fragments.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.createplaylist.models.Playlist

class PlaylistsGridLayoutAdapter(var playlists: ArrayList<Playlist>): RecyclerView.Adapter<PlaylistsGridLayoutViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsGridLayoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_grid, parent, false)
        return PlaylistsGridLayoutViewHolder(view)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistsGridLayoutViewHolder, position: Int) {
       holder.bind(playlists[position])
    }
}