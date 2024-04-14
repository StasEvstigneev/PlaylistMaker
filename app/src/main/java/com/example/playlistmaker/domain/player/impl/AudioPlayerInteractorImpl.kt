package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.search.models.Track

class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository): AudioPlayerInteractor {
    override fun preparePlayer(track: Track) {
        repository.preparePlayer(track)
    }

    override fun setOnPreparedListener(p: () -> Unit) {
        repository.setOnPreparedListener(p)
    }

    override fun setOnCompletionListener(p: () -> Unit) {
        repository.setOnCompletionListener(p)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

    override fun resetTrackPlaybackTime(): String {
        return repository.resetTrackPlaybackTime()
    }

    override fun getCurrentPosition(): String {
        return repository.getCurrentPosition()
    }

}