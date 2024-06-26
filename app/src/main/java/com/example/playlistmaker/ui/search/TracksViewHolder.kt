package com.example.playlistmaker.ui.search


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivTrackArtWork: ImageView = itemView.findViewById(R.id.ivTrackArtWork)
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)


    fun bind(track: Track) {

        val trackArtWorkUrl: String = track.artworkUrl100
        val cornerRadius =
            itemView.context.resources.getDimensionPixelSize(R.dimen.track_artwork_cornerRadius)
        Glide.with(itemView)
            .load(trackArtWorkUrl)
            .placeholder(R.drawable.img_trackplaceholder)
            .centerInside()
            .transform(RoundedCorners(cornerRadius))
            .into(ivTrackArtWork)


        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvTrackTime.text = track.trackTime


    }


}