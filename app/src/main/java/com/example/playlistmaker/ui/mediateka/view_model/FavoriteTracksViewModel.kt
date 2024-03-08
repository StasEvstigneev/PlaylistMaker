package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.mediateka.models.FavoriteTracksState


class FavoriteTracksViewModel(tracksInteractor: TracksInteractor): ViewModel() {

    private var screenStateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeScreenState(): LiveData<FavoriteTracksState> = screenStateLiveData

    init {
        var favoriteTracks = tracksInteractor.getFavoriteTracks()
        if (favoriteTracks.isNotEmpty()) {
            screenStateLiveData.postValue(FavoriteTracksState.FavoriteTracks(favoriteTracks))
        } else {
            screenStateLiveData.postValue(FavoriteTracksState.NoFavoriteTracks)
        }
    }

}