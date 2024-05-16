package com.example.playlistmaker.domain.createplaylist.impl

import android.net.Uri
import com.example.playlistmaker.domain.createplaylist.PlaylistCoversInteractor
import com.example.playlistmaker.domain.createplaylist.PlaylistCoversRepository
import java.io.File

class PlaylistCoversInteractorImpl(private val repository: PlaylistCoversRepository): PlaylistCoversInteractor {
    override suspend fun saveImageToPrivateStorage(uri: Uri, imageName: String):String {
        return repository.saveImageToPrivateStorage(uri, imageName)
    }

    override suspend fun loadImageFromPrivateStorage(): File {
        TODO("Not yet implemented")
    }
}