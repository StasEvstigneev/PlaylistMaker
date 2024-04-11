package com.example.playlistmaker.presentation.mediateka

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.mediateka.models.PlaylistsState

class PlaylistsViewModel(
    private val tracksInteractor: TracksInteractor
): ViewModel() {

    private var screenStateLiveData = MutableLiveData<PlaylistsState>()
    fun observeScreenState(): LiveData<PlaylistsState> = screenStateLiveData

    init {
        screenStateLiveData.postValue(PlaylistsState.NoPlaylistsAvailable)
    }



}