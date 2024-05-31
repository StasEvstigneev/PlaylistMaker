package com.example.playlistmaker.data.db.playlists

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    var id: Int?,
    @ColumnInfo(name = "playlist_title")
    var title: String,
    @ColumnInfo(name = "playlist_description")
    var description: String?,
    @ColumnInfo(name = "playlist_cover")
    var coverPath: String?,
    @ColumnInfo(name = "tracks")
    var tracksIds: String,
    @ColumnInfo(name = "tracks_quantity")
    var tracksQuantity: Int,
    @ColumnInfo(name = "creation_time")
    val timeStamp: Long
)


