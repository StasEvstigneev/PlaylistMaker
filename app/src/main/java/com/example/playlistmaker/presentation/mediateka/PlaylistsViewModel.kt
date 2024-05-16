package com.example.playlistmaker.presentation.mediateka

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.createplaylist.PlaylistsInteractor
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.mediateka.models.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private var screenState = MutableLiveData<PlaylistsState>(PlaylistsState.Loading)
    fun observeScreenState(): LiveData<PlaylistsState> = screenState

    init {
        getPlaylists()

    }


    fun getPlaylists() {
        viewModelScope.launch{
            playlistsInteractor.getPlaylists()
                .collect{
                    playlists -> processResult(playlists)

                }

        }
    }

    private fun processResult(playlists: List<Playlist>) {

        if (playlists.isNotEmpty()) {
            screenState
                .postValue(PlaylistsState.PlaylistsAvailable(playlists as ArrayList<Playlist>))
        } else {
            screenState
                .postValue(PlaylistsState.NoPlaylistAvailable)
        }

    }


}