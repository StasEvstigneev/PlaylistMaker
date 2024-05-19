package com.example.playlistmaker.domain.player.model

sealed class AudioPlayerPlaylistsMessagesState {

    object Default : AudioPlayerPlaylistsMessagesState()

    data class TrackHasBeenAddedToPL(val playlistName: String) : AudioPlayerPlaylistsMessagesState()

    data class TrackHasNotBeenAddedToPL(val playlistName: String) :
        AudioPlayerPlaylistsMessagesState()


}