package com.example.playlistmaker.ui.mediateka.fragments.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistItemGridBinding
import com.example.playlistmaker.domain.createplaylist.models.Playlist

class PlaylistsGridLayoutAdapter(
    var playlists: ArrayList<Playlist>,
    private val clickListener: OnPlaylistClickListener
) :
    RecyclerView.Adapter<PlaylistsGridLayoutViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsGridLayoutViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistsGridLayoutViewHolder(
            PlaylistItemGridBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistsGridLayoutViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onPlaylistClick(playlists[position]) }
    }


    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }


}