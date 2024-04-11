package com.example.playlistmaker.data.mediateka

import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.mediateka.TracksRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.storage.LocalStorage

class TracksRepositoryImpl(
    private val localStorage: LocalStorage,
    private val gsonJsonConverter: GsonJsonConverter

) : TracksRepository {
    override fun addTrackToFavorites(track: Track) {
        val favoriteTracks = getFavoriteTracks()
        favoriteTracks.add(track)
        val json = gsonJsonConverter.getJsonFromArrayList(favoriteTracks)
        localStorage.addStringData(FAVORITE_TRACKS, json)
    }

    override fun getFavoriteTracks(): ArrayList<Track> {
        val json = localStorage.getSavedStringData(FAVORITE_TRACKS)
        return gsonJsonConverter.getTrackListFromJson(json)
    }

    override fun playThisTrack(selectedTrack: Track) {
        val json: String = gsonJsonConverter.getJsonFromTrack(selectedTrack)
        localStorage.addStringData(SELECTED_TRACK_KEY, json)
    }

    override fun receiveTackInPlayer(): Track {
        val json = localStorage.getSavedStringData(SELECTED_TRACK_KEY)
        return gsonJsonConverter.getTrackFromJson(json)
    }


    companion object {
        private const val FAVORITE_TRACKS = "FavoriteTracks"
        private const val SELECTED_TRACK_KEY = "TrackToPlay"
    }


}