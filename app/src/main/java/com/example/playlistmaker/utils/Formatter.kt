package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Formatter {

    fun getTimeMMSS(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(time)
    }

    fun getArtworkUrl512(artworkUrl: String): String {
        return artworkUrl
            .replaceAfterLast('/', "512x512bb.jpg")
    }

    fun getYearFromReleaseDate(releaseDate: String): String {
        return if (releaseDate.length > 4) {
           releaseDate.substring(0, 4)
        } else {
            releaseDate
        }
    }

}