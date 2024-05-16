package com.example.playlistmaker.presentation.createplaylist


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.createplaylist.PlaylistCoversInteractor
import com.example.playlistmaker.domain.createplaylist.PlaylistsInteractor
import com.example.playlistmaker.domain.createplaylist.models.CreatePlaylistState
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import kotlinx.coroutines.launch



class CreatePlaylistViewModel(
    private val playlistCoversInteractor: PlaylistCoversInteractor,
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private var title: String? = null
    private var description: String? = null
    private var image: Uri? = null
    private var imagePath: String? = null


    private val screenState = MutableLiveData<CreatePlaylistState>(CreatePlaylistState.Loading)
            fun getScreenState(): LiveData<CreatePlaylistState> = screenState

    init {
        screenState.postValue(CreatePlaylistState.DataUpdated(title, description, image))
    }

    fun updateTitle(updatedTitle: String) {
        title = updatedTitle
        screenState.postValue(CreatePlaylistState.DataUpdated(title, description, image))
    }

    fun updateDescription(updatedDescription: String) {
        description = updatedDescription
        screenState.postValue(CreatePlaylistState.DataUpdated(title, description, image))

    }

    fun updateImage(updatedImage: Uri) {
        image = updatedImage
        screenState.postValue(CreatePlaylistState.DataUpdated(title, description, image))
    }

    private fun saveImageToPrivateStorage() {
        if (image != null) {
            viewModelScope.launch{
                imagePath = playlistCoversInteractor.saveImageToPrivateStorage(image!!, title!!)
            }
        }
    }

    fun savePlaylist() {
        saveImageToPrivateStorage()

        val newPlaylist = Playlist(null, title!!, description, imagePath)

        viewModelScope.launch {
            playlistsInteractor.addPlaylist(newPlaylist)

        }
    }

}