package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track

interface AudioPlayerInterface {

    fun preparePlayer(track: Track)

    fun setOnPreparedListener(p: () -> Unit)

    fun setOnCompletionListener(p: () -> Unit)


    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun resetTrackPlaybackTime(): String


    fun getCurrentPosition(): String




}