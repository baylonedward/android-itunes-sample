package com.kikimore.android_itunes_sample.data.local

import androidx.room.Dao
import androidx.room.Query
import com.kikimore.android_itunes_sample.data.entities.Track
import kotlinx.coroutines.flow.Flow

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 *
 */
@Dao
interface TrackDao : BaseDao<Track> {

  @Query("SELECT * FROM tracks")
  fun all(): Flow<List<Track>>

  @Query("DELETE FROM tracks")
  suspend fun deleteAll()

  @Query("SELECT * FROM tracks WHERE trackName LIKE '%' || :query || '%' OR genre LIKE '%' || :query || '%'")
  fun search(query: String): Flow<List<Track>>
}