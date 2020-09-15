package com.kikimore.android_itunes_sample.data.remote

import com.kikimore.android_itunes_sample.data.entities.TrackResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 */
interface ITunesService {

  @GET("search")
  suspend fun getMovieByCountry(
    @QueryMap options: Map<String, String>
  ): Response<TrackResponse>
}