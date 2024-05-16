package com.example.playlistmaker.presentation.mediateka

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.mediateka.models.FavoriteTracksState
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.launch


class FavoriteTracksViewModel(private val tracksInteractor: TracksInteractor): ViewModel() {

    private var screenStateLiveData = MutableLiveData<FavoriteTracksState>(FavoriteTracksState.Loading)
    fun observeScreenState(): LiveData<FavoriteTracksState> = screenStateLiveData

    init {
        getFavoriteTracks()
    }

    fun getFavoriteTracks() {
        viewModelScope.launch {
            tracksInteractor.getFavoriteTracks()
                .collect {
                        tracks -> processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {

        if (tracks.isNotEmpty()) {
            screenStateLiveData.postValue(FavoriteTracksState.FavoriteTracks(tracks as ArrayList<Track>))
        } else {
            screenStateLiveData.postValue(FavoriteTracksState.NoFavoriteTracks)
        }
    }

    fun playThisTrack(track: Track) {
        tracksInteractor.playThisTrack(track)
    }



}