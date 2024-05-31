package com.example.playlistmaker.domain.createplaylist.models

data class Playlist(
    var id: Int?,
    var title: String,
    var description: String?,
    var coverPath: String?,
    var tracksIds: ArrayList<Int> = arrayListOf(),
    var tracksQuantity: Int = 0,
    val timeStamp: Long = System.currentTimeMillis()
)
