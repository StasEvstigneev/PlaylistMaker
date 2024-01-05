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
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val playbackTimerUpdater = Runnable {updatePlaybackTimer()}
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }


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
        trackPlaybackTimer.text = getTimeMMSS(INITIAL_TRACK_PLAYBACK_TIME_MILLIS)

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
        mainThreadHandler.removeCallbacksAndMessages(playbackTimerUpdater)
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
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.ic_pause_button)
        playbackTimerUpdater.run()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.setImageResource(R.drawable.ic_play_button)
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

    private fun updatePlaybackTimer() {
        when(playerState) {
            STATE_PLAYING -> {
                trackPlaybackTimer.text = getTimeMMSS(mediaPlayer.currentPosition.toLong())
                mainThreadHandler.postDelayed(playbackTimerUpdater, PLAYER_POSITION_CHECK_INTERVAL_MILLIS)
            }
            STATE_PAUSED -> {mainThreadHandler.removeCallbacks(playbackTimerUpdater)}
            STATE_PREPARED, STATE_DEFAULT -> {
                mainThreadHandler.removeCallbacksAndMessages(playbackTimerUpdater)
                resetTrackPlaybackTime()
            }
        }
    }
    
    private fun getTimeMMSS(time: Long): String {
        return dateFormat.format(time)
    }

    private fun resetTrackPlaybackTime() {
        trackPlaybackTimer.text = getTimeMMSS(INITIAL_TRACK_PLAYBACK_TIME_MILLIS)

    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val INITIAL_TRACK_PLAYBACK_TIME_MILLIS = 0L
        private const val PLAYER_POSITION_CHECK_INTERVAL_MILLIS = 500L
    }

}