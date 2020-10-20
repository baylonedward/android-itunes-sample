package com.kikimore.android_itunes_sample.data.remote

import javax.inject.Inject

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 */
class ITunesRemoteDataSource @Inject constructor(private val iTunesService: ITunesService) :
  BaseDataResource() {

  suspend fun getMovieByCountry(map: Map<String, String>) =
    getResult { iTunesService.getMovieByCountry(map) }
}