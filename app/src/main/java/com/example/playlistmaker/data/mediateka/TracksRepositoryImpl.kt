package com.example.playlistmaker.data.mediateka

import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.mediateka.TracksRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.storage.LocalStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val localStorage: LocalStorage,
    private val gsonJsonConverter: GsonJsonConverter,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor

) : TracksRepository {

    override fun playThisTrack(selectedTrack: Track) {
        val json: String = gsonJsonConverter.getJsonFromTrack(selectedTrack)
        localStorage.addStringData(SELECTED_TRACK_KEY, json)
    }

    override fun uploadTrackInPlayer(): Track {
        val json = localStorage.getSavedStringData(SELECTED_TRACK_KEY)
        return gsonJsonConverter.getTrackFromJson(json)
    }

    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase
            .tracksDao()
            .insertTrack(
                trackDbConvertor
                    .map(track)
            )
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        appDatabase
            .tracksDao()
            .deleteTrack(
                trackDbConvertor
                    .map(track)
            )
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = appDatabase
            .tracksDao()
            .getFavoriteTracks()
        emit(convertFromTrackEntities(favoriteTracks))
    }

    override fun getFavoriteTracksIds(): Flow<List<Int>> = flow {
        val favoriteTrackIds = appDatabase
            .tracksDao()
            .getFavoriteTracksIds()

        emit(favoriteTrackIds)
    }

    private fun convertFromTrackEntities(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }


    companion object {
        private const val SELECTED_TRACK_KEY = "TrackToPlay"
    }


}