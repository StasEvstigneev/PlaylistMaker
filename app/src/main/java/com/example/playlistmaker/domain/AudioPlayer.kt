package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track

interface AudioPlayer {

    fun preparePlayer(track: Track)

    fun setPlayerStatePrepared()

    fun setOnPreparedListener(p: () -> Unit)

    fun setOnCompletionListener(p: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun isPlaying(): Boolean

    fun releasePlayer(runnable: Runnable)

    fun resetTrackPlaybackTime(): String

    fun getCurrentPosition(): String

    fun updatePlaybackTimer(runnable: Runnable): String



}