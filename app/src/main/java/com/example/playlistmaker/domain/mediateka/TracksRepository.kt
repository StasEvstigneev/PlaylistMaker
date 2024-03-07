package com.example.playlistmaker.domain.mediateka

import com.example.playlistmaker.domain.search.models.Track

interface TracksRepository {

    fun addTrackToFavorites(track: Track)

    fun getFavoriteTracks(): ArrayList<Track>

    fun selectTrackForPlayer(selectedTrack: Track)

    fun receiveTackInPlayer(): Track


}