package com.example.playlistmaker.domain.player.model

enum class AudioPlayerStatus(val term: Boolean) {
    DEFAULT(false),
    PREPARED(true),
    PLAYING(true),
    PAUSED(false)
}