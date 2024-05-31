package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.favorite_tracks.TrackEntity
import com.example.playlistmaker.data.db.favorite_tracks.TracksDao
import com.example.playlistmaker.data.db.general_playlist.GeneralPlaylistDao
import com.example.playlistmaker.data.db.general_playlist.GeneralPlaylistEntity
import com.example.playlistmaker.data.db.playlists.PlaylistEntity
import com.example.playlistmaker.data.db.playlists.PlaylistsDao

@Database(
    version = 77,
    entities = [TrackEntity::class, PlaylistEntity::class, GeneralPlaylistEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tracksDao(): TracksDao

    abstract fun playlistsDao(): PlaylistsDao

    abstract fun generalPlaylistDao(): GeneralPlaylistDao

}