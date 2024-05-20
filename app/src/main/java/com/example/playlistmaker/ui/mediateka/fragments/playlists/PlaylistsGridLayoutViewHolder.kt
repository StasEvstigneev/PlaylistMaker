package com.example.playlistmaker.ui.mediateka.fragments.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemGridBinding
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.utils.Formatter

class PlaylistsGridLayoutViewHolder(private val binding: PlaylistItemGridBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {

        Glide.with(itemView)
            .load(playlist.coverPath)
            .placeholder(R.drawable.img_trackplaceholder)
            .centerCrop()
            .into(binding.playlistCoverGrid)

        binding.playlistTitleGrid.text = playlist.title

        binding.playlistTracksQuantityGrid.text =
            Formatter.playlistTracksQuantityFormatter(playlist.tracksQuantity, itemView.context)

    }

}