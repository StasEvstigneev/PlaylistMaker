package com.example.playlistmaker.presentation

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
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.Formatter
import com.example.playlistmaker.R
import com.example.playlistmaker.data.GsonJsonConverterImpl
import com.example.playlistmaker.domain.Constants
import com.example.playlistmaker.domain.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecase.audioplayer.ReceiveTrackInPlayer


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

    private val audioPlayer = Creator.getAudioPlayer()

    private lateinit var searchHistoryRepository: SearchHistoryRepository
    private lateinit var receiveTrack: ReceiveTrackInPlayer

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val playbackTimerUpdater = Runnable {updatePlaybackTimer()}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        searchHistoryRepository = Creator.provideSearchHistoryRepository(context = applicationContext)
        receiveTrack = Creator.provideReceiveTrackInPlayerUseCase(searchHistoryRepository, GsonJsonConverterImpl)

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


        returnButton = findViewById(R.id.iv_arrowReturn)
        returnButton.setOnClickListener {
            this.finish()
        }

        track = receiveTrack.execute()


        val coverCornerRadius =
            resources.getDimensionPixelSize(R.dimen.audio_player_artWork_cornerRadius)
        Glide.with(applicationContext)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.img_player_album_placeholder)
            .transform(RoundedCorners(coverCornerRadius))
            .into(playerArtwork)

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = track.trackTime
        trackPlaybackTimer.text = Formatter.getTimeMMSS(Constants.INITIAL_TRACK_PLAYBACK_TIME_MILLIS)

        if (track.collectionName.isEmpty()) {
            groupTrackAlbum.isVisible = false
        } else {
            groupTrackAlbum.isVisible = true
            trackAlbum.text = track.collectionName
        }
        trackYear.text = Formatter.getYearFromReleaseDate(track.releaseDate)
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
        audioPlayer.releasePlayer()
    }



    private fun preparePlayer() {
        audioPlayer.preparePlayer(track)
        audioPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            audioPlayer.playerState = Constants.PLAYER_STATE_PREPARED
        }
        audioPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_play_button)
            audioPlayer.playerState = Constants.PLAYER_STATE_PREPARED
        }
    }


    private fun startPlayer() {
        audioPlayer.startPlayer()
        playButton.setImageResource(R.drawable.ic_pause_button)
        playbackTimerUpdater.run()
    }


    private fun pausePlayer() {
        audioPlayer.pausePlayer()
        playButton.setImageResource(R.drawable.ic_play_button)
    }

    private fun playbackControl() {
        when (audioPlayer.playerState) {
            Constants.PLAYER_STATE_PREPARED, Constants.PLAYER_STATE_PAUSED -> {
                startPlayer()
            }
            Constants.PLAYER_STATE_PLAYING -> {
                pausePlayer()
            }
        }
    }


    private fun updatePlaybackTimer() {
        when(audioPlayer.playerState) {
            Constants.PLAYER_STATE_PLAYING -> {
                trackPlaybackTimer.text = audioPlayer.getCurrentPosition()
                mainThreadHandler.postDelayed(playbackTimerUpdater, Constants.PLAYER_POSITION_CHECK_INTERVAL_MILLIS)
            }
            Constants.PLAYER_STATE_PAUSED -> {mainThreadHandler.removeCallbacks(playbackTimerUpdater)}
            Constants.PLAYER_STATE_PREPARED, Constants.PLAYER_STATE_DEFAULT -> {
                mainThreadHandler.removeCallbacksAndMessages(playbackTimerUpdater)
                resetTrackPlaybackTime()
            }
        }
    }

    private fun resetTrackPlaybackTime() {
        trackPlaybackTimer.text = audioPlayer.resetTrackPlaybackTime()

    }


}