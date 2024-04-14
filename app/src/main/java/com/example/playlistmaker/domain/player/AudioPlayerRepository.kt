package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.search.models.Track

interface AudioPlayerRepository {

    fun preparePlayer(track: Track)

    fun setOnPreparedListener(p: () -> Unit)

    fun setOnCompletionListener(p: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun isPlaying(): Boolean

    fun resetTrackPlaybackTime(): String

    fun getCurrentPosition(): String

}