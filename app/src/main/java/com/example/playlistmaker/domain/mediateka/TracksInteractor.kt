package com.example.playlistmaker.domain.mediateka

import com.example.playlistmaker.domain.search.models.Track

interface TracksInteractor {

    fun addTrackToFavorites(track: Track)

    fun getFavoriteTracks(): ArrayList<Track>

    fun playThisTrack(selectedTrack: Track)

    fun uploadTrackInPlayer(): Track


}