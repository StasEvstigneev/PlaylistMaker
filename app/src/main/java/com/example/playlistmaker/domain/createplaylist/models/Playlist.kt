package com.example.playlistmaker.domain.createplaylist.models

data class Playlist(
    var id: Int?,
    val title: String,
    val description: String?,
    val coverPath: String?,
    val trackIds: ArrayList<Int> = arrayListOf(),
    var tracksQuantity: Int = 0,
    val timeStamp: Long = System.currentTimeMillis()
)
