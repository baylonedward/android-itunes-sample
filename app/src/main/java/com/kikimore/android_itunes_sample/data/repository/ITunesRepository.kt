package com.kikimore.android_itunes_sample.data.repository

import com.kikimore.android_itunes_sample.data.entities.Track
import com.kikimore.android_itunes_sample.data.local.TrackDao
import com.kikimore.android_itunes_sample.data.remote.ITunesRemoteDataSource
import com.kikimore.android_itunes_sample.data.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

  fun getMovieByCountry(query: String, country: String): Flow<Resource<List<Track>>> = channelFlow {
    val map = mapOf("term" to query, "country" to country, "media" to "movie")
    // return local data first
    launch { localDataSource.search(query).collect { send(Resource.success(it)) } }
    // invoke network call
    val response = remoteDataSource.getMovieByCountry(map)
    when (response.status) {
      Resource.Status.SUCCESS -> {
        response.data?.also {
          // save to local db
          localDataSource.insert(it.results)
        }
      }
      Resource.Status.ERROR -> send(Resource.error(message = response.message ?: "Something Went Wrong", data = null))
      else -> {
      }
    }
  }
}