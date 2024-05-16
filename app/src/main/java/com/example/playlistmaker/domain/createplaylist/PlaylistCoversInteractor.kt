package com.example.playlistmaker.domain.createplaylist

import android.net.Uri
import java.io.File

interface PlaylistCoversInteractor {

    suspend fun saveImageToPrivateStorage(uri: Uri, imageName: String): String

    suspend fun loadImageFromPrivateStorage(): File


}