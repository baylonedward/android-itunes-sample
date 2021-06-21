package com.kikimore.android_itunes_sample.data.repository

import com.kikimore.android_itunes_sample.data.entities.Track
import com.kikimore.android_itunes_sample.data.local.TrackDao
import com.kikimore.android_itunes_sample.data.remote.ITunesRemoteDataSource
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.data.utils.performGetOperation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 *
 * Central source of data from both local and network
 */

@ExperimentalCoroutinesApi
@Singleton
class ITunesRepository @Inject constructor(
  private val remoteDataSource: ITunesRemoteDataSource,
  private val localDataSource: TrackDao
) {

  fun getMovieByCountry(query: String, country: String): Flow<Resource<List<Track>>> {
    val map = mapOf("term" to query, "country" to country, "media" to "movie")
    return performGetOperation(
      databaseQuery = { localDataSource.all() },
      networkCall = { remoteDataSource.getMovieByCountry(map) },
      saveCallResult = { localDataSource.insert(it.results) }
    )
  }
}