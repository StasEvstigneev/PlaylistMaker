package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.general_playlist.GeneralPlaylistEntity
import com.example.playlistmaker.domain.search.models.Track

class GeneralPlaylistDbConverter {

    fun map(track: Track): GeneralPlaylistEntity {
        return GeneralPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.artworkUrl512,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: GeneralPlaylistEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.artworkUrl512,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

}