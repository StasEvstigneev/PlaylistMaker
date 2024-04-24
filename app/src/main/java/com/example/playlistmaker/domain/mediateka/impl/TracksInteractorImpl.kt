package com.example.playlistmaker.domain.mediateka.impl

import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.mediateka.TracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun playThisTrack(selectedTrack: Track) {
        repository.playThisTrack(selectedTrack)
    }

    override fun uploadTrackInPlayer(): Track {
        return repository.uploadTrackInPlayer()
    }

    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        repository.removeTrackFromFavorites(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
    }

    override fun getFavoriteTracksIds(): Flow<List<Int>> {
        return repository.getFavoriteTracksIds()
    }
}