package com.example.playlistmaker.data.db.general_playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.favorite_tracks.TrackEntity


@Dao
interface GeneralPlaylistDao {

    @Insert(entity = GeneralPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInGeneralPlaylist(track: GeneralPlaylistEntity)

    @Delete(entity = GeneralPlaylistEntity::class)
    suspend fun deleteTrackFromGeneralPlaylist(track: GeneralPlaylistEntity)


    @Query("SELECT * FROM general_playlist ORDER BY insertion_time asc")
    suspend fun getTracksFromGeneralPlaylist(): List<GeneralPlaylistEntity>

    @Query("SELECT track_id FROM general_playlist")
    suspend fun getTracksIds(): List<Int>



}