package com.example.playlistmaker.ui.player

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.utils.Formatter

class PlaylistsLinearLayoutViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val cover = itemView.findViewById<ImageView>(R.id.playlistCoverLinearLayout)
    private val title = itemView.findViewById<TextView>(R.id.playlistTitleLinearLayout)
    private val tracksQuantity = itemView.findViewById<TextView>(R.id.playlistTracksQuantityLinearLayout)

    fun bind(playlist: Playlist) {

        Glide.with(itemView)
            .load(playlist.coverPath)
            .placeholder(R.drawable.img_trackplaceholder)
            .centerCrop()
            .into(cover)


        title.text = playlist.title
        tracksQuantity.text = Formatter.playlistTracksQuantityFormatter(playlist.tracksQuantity, itemView.context)




    }


}