package com.example.playlistmaker.domain.player.model

import com.example.playlistmaker.domain.search.models.Track

sealed class AudioPlayerScreenState {
    object Loading: AudioPlayerScreenState()
    data class Content(val track: Track): AudioPlayerScreenState()
}