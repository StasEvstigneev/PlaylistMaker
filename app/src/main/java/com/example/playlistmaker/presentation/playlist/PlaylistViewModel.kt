package com.example.playlistmaker.presentation.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.createplaylist.PlaylistsInteractor
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import com.example.playlistmaker.domain.mediateka.TracksInteractor
import com.example.playlistmaker.domain.playlist.BottomSheetState
import com.example.playlistmaker.domain.playlist.PlaylistScreenState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.sharing.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val tracksInteractor: TracksInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    var playlist: Playlist? = null

    private var playlistState = MutableLiveData<Playlist>()
    fun getPlaylistState(): LiveData<Playlist> = playlistState


    fun updatePlaylist(playlist: Playlist) {
        this.playlist = playlist
    }


    private var screenState = MutableLiveData<PlaylistScreenState>(PlaylistScreenState.Loading)
    fun getScreenState(): LiveData<PlaylistScreenState> = screenState

    private var bottomSheetState = MutableLiveData<BottomSheetState>(BottomSheetState.NoTracks())
    fun getBottomSheetState(): LiveData<BottomSheetState> = bottomSheetState


    init {
        screenState.postValue(PlaylistScreenState.Loading)
        bottomSheetState.postValue(BottomSheetState.NoTracks())

    }

    fun getPlaylistById(id: Int) {

        viewModelScope.launch {
            val playlist = playlistsInteractor.getPlaylistById(id)

            playlistState.postValue(playlist)

            viewModelScope.launch {
                playlistsInteractor.getTracksByIds(playlist.tracksIds).collect { tracks ->
                    updateTracks(tracks as ArrayList<Track>)

                    screenState.postValue(
                        PlaylistScreenState.PlaylistData(
                            playlist.title,
                            playlist.description ?: "",
                            playlist.coverPath,
                            playlistsInteractor.getTracklistDuration(tracks),
                            playlist.tracksQuantity
                        )
                    )

                }

            }
        }

    }

    private fun updateTracks(tracks: ArrayList<Track>) {
        if (!tracks.isNullOrEmpty()) {
            bottomSheetState.postValue(BottomSheetState.TracksAvailable(tracks = tracks))
        } else {
            bottomSheetState.postValue(BottomSheetState.NoTracks())
        }

    }

    fun playThisTrack(selectedTrack: Track) {
        tracksInteractor.playThisTrack(selectedTrack)
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrackFromPlaylist(playlist!!, track)
        }
        updatePlaylistData()
    }

    fun updatePlaylistData() {
        getPlaylistById(playlist!!.id!!)
    }

    fun sharePlaylist(tracks: ArrayList<Track>) {
        sharingInteractor.sharePlaylist(playlist!!, tracks)
    }


    fun deletePlaylist(tracks: ArrayList<Track>) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                for (track in tracks) {
                    playlistsInteractor.deleteTrackFromPlaylist(playlist!!, track)
                }
                playlistsInteractor.deletePlaylist(playlist!!)
            }
        }
    }


}