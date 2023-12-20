package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var returnButton: ImageView
    private lateinit var playerArtwork: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var playButton: ImageView
    private lateinit var addToPlaylistButton: ImageView
    private lateinit var addToFavoritesButton: ImageView
    private lateinit var remainingTrackTime: TextView
    private lateinit var trackDuration: TextView
    private lateinit var trackAlbum: TextView
    private lateinit var trackYear: TextView
    private lateinit var trackGenre: TextView
    private lateinit var trackCountry: TextView
    private lateinit var groupTrackAlbum: Group


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        returnButton = findViewById(R.id.iv_arrowReturn)
        returnButton.setOnClickListener {
            this.finish()
        }

        playerArtwork = findViewById(R.id.iv_playerArtwork)
        trackName = findViewById(R.id.tv_trackName)
        artistName = findViewById(R.id.tv_artistName)
        playButton = findViewById(R.id.iv_playButton)
        addToPlaylistButton = findViewById(R.id.iv_addToPlaylistButton)
        addToFavoritesButton = findViewById(R.id.iv_addToFavoritesButton)
        remainingTrackTime = findViewById(R.id.tv_trackTimeRemaining)
        trackDuration = findViewById(R.id.tv_trackDuration)
        trackAlbum = findViewById(R.id.tv_trackAlbum)
        trackYear = findViewById(R.id.tv_trackYear)
        trackGenre = findViewById(R.id.tv_trackGenre)
        trackCountry = findViewById(R.id.tv_trackCountry)
        groupTrackAlbum = findViewById(R.id.trackAlbumGroup)

        val track = intent.getSerializableExtra(
            INTENT_KEY_FOR_TRACK
        ) as Track
        val trackCoverUrl: String = track.getArtworkUrl512()
        val coverCornerRadius =
            resources.getDimensionPixelSize(R.dimen.audio_player_artWork_cornerRadius)
        Glide.with(applicationContext)
            .load(trackCoverUrl)
            .placeholder(R.drawable.img_player_album_placeholder)
            .transform(RoundedCorners(coverCornerRadius))
            .into(playerArtwork)

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = track.getTrackTimeMMSS()
        remainingTrackTime.text = trackDuration.text //временное значение

        if (track.collectionName.isEmpty()) {
            groupTrackAlbum.isVisible = true
        } else {
            groupTrackAlbum.isVisible = false
            trackAlbum.text = track.collectionName
        }
        trackYear.text = track.releaseDate.substring(0, 4)
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country


    }

}