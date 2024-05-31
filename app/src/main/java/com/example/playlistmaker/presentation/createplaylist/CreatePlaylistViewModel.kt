package com.example.playlistmaker.presentation.createplaylist


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.createplaylist.PlaylistsInteractor
import com.example.playlistmaker.domain.createplaylist.models.CreatePlaylistState
import com.example.playlistmaker.domain.createplaylist.models.Playlist
import kotlinx.coroutines.launch


open class CreatePlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    open var title: String? = null
    open var description: String? = null
    open var image: Uri? = null
    open var imagePath: String? = null


    private val screenState = MutableLiveData<CreatePlaylistState>(CreatePlaylistState.Loading)
    fun getScreenState(): LiveData<CreatePlaylistState> = screenState


    private val imageState = MutableLiveData<Uri?>(image)
    fun getImageState(): LiveData<Uri?> = imageState


    init {
        screenState.postValue(CreatePlaylistState.DataUpdated(title, description))
        imageState.postValue(image)
    }

    fun updateTitle(updatedTitle: String) {
        title = updatedTitle
        screenState.postValue(CreatePlaylistState.DataUpdated(title, description))
    }

    fun updateDescription(updatedDescription: String?) {
        description = updatedDescription
        screenState.postValue(CreatePlaylistState.DataUpdated(title, description))

    }

    fun updateImage(updatedImage: Uri?) {
        image = updatedImage
        imageState.postValue(image)
    }

    fun updateImagePath(path: String?) {
        imagePath = path
    }


    fun savePlaylist() {
        val newPlaylist = Playlist(null, title!!, description, imagePath)

        viewModelScope.launch {
            playlistsInteractor.addPlaylist(newPlaylist)

        }
    }

}