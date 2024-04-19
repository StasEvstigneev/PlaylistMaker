package com.example.playlistmaker.domain.mediateka

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    fun playThisTrack(selectedTrack: Track)

    fun uploadTrackInPlayer(): Track

    suspend fun addTrackToFavorites(track: Track)

    suspend fun removeTrackFromFavorites(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>

    fun getFavoriteTracksIds(): Flow<List<Int>>


}