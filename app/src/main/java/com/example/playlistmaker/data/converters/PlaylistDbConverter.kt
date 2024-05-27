package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.domain.GsonJsonConverter
import com.example.playlistmaker.domain.createplaylist.models.Playlist

class PlaylistDbConverter(private val gsonJsonConverter: GsonJsonConverter) {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.coverPath,
            gsonJsonConverter.getJsonFromTrackIds(playlist.tracksIds),
            playlist.tracksQuantity,
            playlist.timeStamp

        )
    }


    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.coverPath,
            gsonJsonConverter.getTrackIdsFromJson(playlist.tracksIds),
            playlist.tracksQuantity,
            playlist.timeStamp
        )
    }


    fun mapTracksIds(tracksIds: String): ArrayList<Int> {
        return gsonJsonConverter.getTrackIdsFromJson(tracksIds)

    }

    fun mapTracksIds(tracksIds: ArrayList<Int>): String {
        return gsonJsonConverter.getJsonFromTrackIds(tracksIds)

    }


}