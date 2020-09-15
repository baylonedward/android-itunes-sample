package com.kikimore.android_itunes_sample.data.repository

import com.kikimore.android_itunes_sample.data.entities.Track
import com.kikimore.android_itunes_sample.data.local.TrackDao
import com.kikimore.android_itunes_sample.data.remote.ITunesRemoteDataSource
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.data.utils.performGetOperation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 */

@ExperimentalCoroutinesApi
class ITunesRepository(
  private val remoteDataSource: ITunesRemoteDataSource,
  private val localDataSource: TrackDao
) {

  /**
   * Be default we are setting country to au
   */
  fun getMovieByCountry(query: String, country: String = "au"): Flow<Resource<List<Track>>> {
    val map = mapOf("term" to query, "country" to country, "media" to "movie")
    return performGetOperation(
      databaseQuery = { localDataSource.all() },
      networkCall = { remoteDataSource.getMovieByCountry(map) },
      saveCallResult ={ localDataSource.insert(it.results) }
    )
  }
}