package com.example.playlistmaker.domain.createplaylist.models


sealed class CreatePlaylistState {

    data object Loading : CreatePlaylistState()
    data class DataUpdated(
        val title: String?,
        val description: String?
    ) : CreatePlaylistState()
}