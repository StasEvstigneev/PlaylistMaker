package com.example.playlistmaker.data.createplaylist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.playlistmaker.domain.createplaylist.PlaylistCoversRepository
import java.io.File
import java.io.FileOutputStream

class PlaylistCoversRepositoryImpl(
    private val context: Context
): PlaylistCoversRepository {
    override suspend fun saveImageToPrivateStorage(uri: Uri, imageName: String): String {

        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), COVERS_ALBUM)

        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val file = File(filePath, "$imageName.jpg")

        val inputStream = context.contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        Log.d("Playlist cover repository", "Изображение сохранено по адресу: ${file.path}")

        return file.path


    }

    override suspend fun loadImageFromPrivateStorage(): File {
        TODO("Not yet implemented")
    }

    companion object {
        const val COVERS_ALBUM = "Playlistmaker covers"
    }

}