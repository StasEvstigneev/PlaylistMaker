package com.example.playlistmaker.domain.player.model

sealed class AudioPlayerState(
    val isPlayButtonEnabled: Boolean,
    val isPlaying: Boolean
) {

    class Default : AudioPlayerState(false, false)
    class Prepared : AudioPlayerState(true, false)

    class Playing(isPlaying: Boolean) : AudioPlayerState(true, isPlaying)

    class Paused(isPlaying: Boolean) : AudioPlayerState(true, isPlaying)

}