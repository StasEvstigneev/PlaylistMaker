package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivTrackArtWork: ImageView = itemView.findViewById(R.id.ivTrackArtWork)
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)


    fun bind(track: Track) {
        val trackArtWorkUrl: String = track.artworkUrl100
        Glide.with(itemView)
            .load(trackArtWorkUrl)
            .placeholder(R.drawable.img_trackplaceholder)
            .centerInside()
            .transform(RoundedCorners(2))
            .into(ivTrackArtWork)

        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvTrackTime.text = track.trackTime

    }


}