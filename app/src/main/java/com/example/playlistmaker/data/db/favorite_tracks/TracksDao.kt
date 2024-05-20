package com.example.playlistmaker.data.db.favorite_tracks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.favorite_tracks.TrackEntity

@Dao
interface TracksDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY insertion_time asc")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT track_id FROM favorite_tracks")
    suspend fun getFavoriteTracksIds(): List<Int>


}