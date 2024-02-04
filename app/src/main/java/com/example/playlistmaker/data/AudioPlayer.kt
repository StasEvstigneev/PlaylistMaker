package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.Constants
import com.example.playlistmaker.domain.Formatter
import com.example.playlistmaker.domain.AudioPlayerInterface
import com.example.playlistmaker.domain.models.Track

class AudioPlayer(val mediaPlayer: MediaPlayer): AudioPlayerInterface {


    var playerState = Constants.PLAYER_STATE_DEFAULT

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun setOnPreparedListener(p: () -> Unit) {
        mediaPlayer.setOnPreparedListener { p() }
    }

    override fun setOnCompletionListener(p: () -> Unit) {
        mediaPlayer.setOnCompletionListener { p() }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = Constants.PLAYER_STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = Constants.PLAYER_STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun resetTrackPlaybackTime(): String {
        return Formatter.getTimeMMSS(Constants.INITIAL_TRACK_PLAYBACK_TIME_MILLIS)

    }

    override fun getCurrentPosition(): String {
        return Formatter.getTimeMMSS(mediaPlayer.currentPosition.toLong())
    }

}