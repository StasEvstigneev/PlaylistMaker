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
            gsonJsonConverter.getJsonFromTrackIds(playlist.trackIds),
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
            gsonJsonConverter.getTrackIdsFromJson(playlist.trackIds),
            playlist.tracksQuantity,
            playlist.timeStamp
        )
    }

}