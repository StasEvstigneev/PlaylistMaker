package com.example.playlistmaker.domain.playlist

sealed class PlaylistScreenState {

    data object Loading : PlaylistScreenState()
    data class PlaylistData(
        val title: String,
        val description: String,
        val coverPath: String?,
        val totalDuration: Long,
        val tracksQuantity: Int
    ) : PlaylistScreenState()

}