package com.example.playlistmaker.domain.mediateka.models

import com.example.playlistmaker.domain.createplaylist.models.Playlist

sealed class PlaylistsState {

    object Loading : PlaylistsState()

    object NoPlaylistAvailable : PlaylistsState()

    data class PlaylistsAvailable(
        val playlists: ArrayList<Playlist>
    ) : PlaylistsState()


}