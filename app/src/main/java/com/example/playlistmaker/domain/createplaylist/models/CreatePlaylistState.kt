package com.example.playlistmaker.domain.createplaylist.models

import android.net.Uri

sealed class CreatePlaylistState {

    object Loading: CreatePlaylistState()
    data class DataUpdated(
        val title: String?,
        val description: String?,
        val image: Uri?
    ): CreatePlaylistState()
}