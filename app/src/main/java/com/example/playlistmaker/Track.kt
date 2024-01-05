package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Serializable {

    fun getTrackTimeMMSS(): String = SimpleDateFormat("mm:ss", Locale.getDefault())
        .format(trackTime)


    fun getArtworkUrl512(): String = artworkUrl100
        .replaceAfterLast('/', "512x512bb.jpg")

}