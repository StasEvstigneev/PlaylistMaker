package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.utils.Formatter
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.search.models.Track

class AudioPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : AudioPlayerRepository {

    private var isPlaying = false

    override fun preparePlayer(track: Track) {
        if (track.previewUrl.isNotEmpty()) {
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        isPlaying = true
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        isPlaying = false
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun getCurrentPosition(): String {
        return Formatter.getTimeMMSS(mediaPlayer.currentPosition.toLong())
    }


    override fun setOnPreparedListener(p: () -> Unit) {
        mediaPlayer.setOnPreparedListener { p() }
    }


    override fun setOnCompletionListener(p: () -> Unit) {
        mediaPlayer.setOnCompletionListener { p() }
        isPlaying = false
    }


    override fun resetTrackPlaybackTime(): String {
        return Formatter.getTimeMMSS(RESET_PLAYBACK_TIME_MILLIS)

    }

    companion object {
        private const val RESET_PLAYBACK_TIME_MILLIS = 0L
    }


}