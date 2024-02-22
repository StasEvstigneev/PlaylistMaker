package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.utils.Formatter
import com.example.playlistmaker.domain.player.AudioPlayer
import com.example.playlistmaker.domain.search.models.Track

class AudioPlayerImpl(val mediaPlayer: MediaPlayer): AudioPlayer {


    private var playerState = PLAYER_STATE_DEFAULT
    private val mainThreadHandler = Handler(Looper.getMainLooper())



    override fun preparePlayer(track: Track) {
        if (track.previewUrl.isNotEmpty()) {
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
        }
    }

    override fun setPlayerStatePrepared() {
        playerState = PLAYER_STATE_PREPARED
    }

    override fun setOnPreparedListener(p: () -> Unit) {
        mediaPlayer.setOnPreparedListener { p() }
    }

    override fun setOnCompletionListener(p: () -> Unit) {
        mediaPlayer.setOnCompletionListener { p() }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PLAYER_STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PLAYER_STATE_PAUSED
    }

    override fun isPlaying(): Boolean {
        return when (playerState) {
            PLAYER_STATE_PREPARED, PLAYER_STATE_PAUSED -> {
                false
            }
            else -> {
                true
            }
        }
    }

    override fun releasePlayer(runnable: Runnable) {
        mainThreadHandler.removeCallbacksAndMessages(runnable)
        mediaPlayer.release()
    }

    override fun resetTrackPlaybackTime(): String {
        return Formatter.getTimeMMSS(INITIAL_TRACK_PLAYBACK_TIME_MILLIS)

    }

    override fun getCurrentPosition(): String {
        return Formatter.getTimeMMSS(mediaPlayer.currentPosition.toLong())
    }

    override fun updatePlaybackTimer(runnable: Runnable): String {
        var trackPlaybackTimer = getCurrentPosition()
        when(playerState) {
            PLAYER_STATE_PLAYING -> {
                trackPlaybackTimer = getCurrentPosition()
                mainThreadHandler.postDelayed(runnable, PLAYER_POSITION_CHECK_INTERVAL_MILLIS)
            }
            PLAYER_STATE_PAUSED -> {mainThreadHandler.removeCallbacks(runnable)}
            PLAYER_STATE_PREPARED, PLAYER_STATE_DEFAULT -> {
                mainThreadHandler.removeCallbacksAndMessages(runnable)
                trackPlaybackTimer = resetTrackPlaybackTime()
            }
        }
        return trackPlaybackTimer
    }


    companion object{
        private const val PLAYER_STATE_DEFAULT = 0
        private const val PLAYER_STATE_PREPARED = 1
        private const val PLAYER_STATE_PLAYING = 2
        private const val PLAYER_STATE_PAUSED = 3
        private const val INITIAL_TRACK_PLAYBACK_TIME_MILLIS = 0L
        private const val PLAYER_POSITION_CHECK_INTERVAL_MILLIS = 200L
    }

}