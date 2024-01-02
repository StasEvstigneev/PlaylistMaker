package com.example.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var returnButton: ImageView
    private lateinit var playerArtwork: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var playButton: ImageView
    private lateinit var addToPlaylistButton: ImageView
    private lateinit var addToFavoritesButton: ImageView
    private lateinit var trackPlaybackTimer: TextView
    private lateinit var trackDuration: TextView
    private lateinit var trackAlbum: TextView
    private lateinit var trackYear: TextView
    private lateinit var trackGenre: TextView
    private lateinit var trackCountry: TextView
    private lateinit var groupTrackAlbum: Group
    private lateinit var track: Track
    val mainThreadHandler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        track = receiveTrack()

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
        trackPlaybackTimer = findViewById(R.id.tv_trackPlaybackTimer)
        trackDuration = findViewById(R.id.tv_trackDuration)
        trackAlbum = findViewById(R.id.tv_trackAlbum)
        trackYear = findViewById(R.id.tv_trackYear)
        trackGenre = findViewById(R.id.tv_trackGenre)
        trackCountry = findViewById(R.id.tv_trackCountry)
        groupTrackAlbum = findViewById(R.id.trackAlbumGroup)


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
        trackPlaybackTimer.text = getTimeMMSS(INITIAL_TRACK_PLAYBACK_TIME)

        if (track.collectionName.isEmpty()) {
            groupTrackAlbum.isVisible = true
        } else {
            groupTrackAlbum.isVisible = false
            trackAlbum.text = track.collectionName
        }
        trackYear.text = track.releaseDate.substring(0, 4)
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        preparePlayer()
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(launchTrackTimer())
        mediaPlayer.release()
    }

    private fun receiveTrack(): Track {
        return intent.getSerializableExtra(
            INTENT_KEY_FOR_TRACK
        ) as Track
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play_button)
            playerState = STATE_PREPARED
            resetTrackPlaybackTime()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause_button)
        playerState = STATE_PLAYING
        mainThreadHandler.post(launchTrackTimer())

    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_play_button)
        playerState = STATE_PAUSED
        mainThreadHandler.removeCallbacks(launchTrackTimer())
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
            STATE_PLAYING -> {
                pausePlayer()
            }
        }
    }

    private fun launchTrackTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    trackPlaybackTimer.text = getTimeMMSS(mediaPlayer.currentPosition.toLong())
                    mainThreadHandler.postDelayed(this, PLAYER_POSITION_CHECK_INTERVAL)
                }
            }
        }
    }

    private fun getTimeMMSS(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    private fun resetTrackPlaybackTime() {
        trackPlaybackTimer.text = getTimeMMSS(INITIAL_TRACK_PLAYBACK_TIME)

    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val INITIAL_TRACK_PLAYBACK_TIME = 0L
        private const val PLAYER_POSITION_CHECK_INTERVAL = 500L
    }

}