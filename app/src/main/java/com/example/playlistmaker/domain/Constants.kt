package com.example.playlistmaker.domain

object Constants {

    const val SHARED_PREFS_NAME = "SharedPrefs"
    const val SEARCH_HISTORY_KEY = "SearchHistoryKey"
    const val SEARCH_HISTORY_ITEMS_LIMIT: Int = 10
    const val SELECTED_TRACK_KEY = "TrackToPlay"

    const val PLAYER_STATE_DEFAULT = 0
    const val PLAYER_STATE_PREPARED = 1
    const val PLAYER_STATE_PLAYING = 2
    const val PLAYER_STATE_PAUSED = 3
    const val INITIAL_TRACK_PLAYBACK_TIME_MILLIS = 0L
    const val PLAYER_POSITION_CHECK_INTERVAL_MILLIS = 200L

}