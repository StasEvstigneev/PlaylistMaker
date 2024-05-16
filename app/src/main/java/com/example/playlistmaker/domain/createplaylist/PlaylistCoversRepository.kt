package com.example.playlistmaker.domain.createplaylist

import android.net.Uri
import java.io.File


interface PlaylistCoversRepository {

    suspend fun saveImageToPrivateStorage(uri: Uri, imageName: String): String

    suspend fun loadImageFromPrivateStorage(): File


}