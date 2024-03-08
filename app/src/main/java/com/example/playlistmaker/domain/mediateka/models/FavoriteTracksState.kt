package com.example.playlistmaker.domain.mediateka.models

import com.example.playlistmaker.domain.search.models.Track
import java.util.ArrayList

sealed class FavoriteTracksState {
    object NoFavoriteTracks : FavoriteTracksState()
    data class FavoriteTracks(val favoriteTracks: ArrayList<Track>) : FavoriteTracksState()
}