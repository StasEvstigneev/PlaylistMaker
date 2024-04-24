package com.example.playlistmaker.domain.mediateka.models

import com.example.playlistmaker.domain.search.models.Track


sealed interface FavoriteTracksState {
    data object Loading: FavoriteTracksState
    data object NoFavoriteTracks : FavoriteTracksState
    data class FavoriteTracks(val favoriteTracks: List<Track>) : FavoriteTracksState
}