package com.example.playlistmaker.domain.mediateka.impl

import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.mediateka.TracksRepository
import com.example.playlistmaker.domain.search.models.Track

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override fun getFavoriteTracks(): ArrayList<Track> {
        return repository.getFavoriteTracks()
    }

    override fun playThisTrack(selectedTrack: Track) {
        repository.playThisTrack(selectedTrack)
    }

    override fun uploadTrackInPlayer(): Track {
        return repository.receiveTackInPlayer()
    }
}