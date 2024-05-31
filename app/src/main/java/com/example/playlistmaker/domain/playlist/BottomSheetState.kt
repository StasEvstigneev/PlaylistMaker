package com.example.playlistmaker.domain.playlist

import com.example.playlistmaker.domain.search.models.Track

sealed class BottomSheetState {

    data class NoTracks(
        val showBottomSheet: Boolean = false
    ): BottomSheetState()

    data class TracksAvailable(
        val showBottomSheet: Boolean = true,
        val tracks: ArrayList<Track>
    ): BottomSheetState()

}