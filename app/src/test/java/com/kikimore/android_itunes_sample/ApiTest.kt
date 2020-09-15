package com.kikimore.android_itunes_sample

import com.kikimore.android_itunes_sample.data.remote.ITunesService
import com.kikimore.android_itunes_sample.data.utils.loggingInterceptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 */

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ApiTest {
  private val baseUrl = "https://itunes.apple.com"
  private lateinit var retrofit: Retrofit
  private lateinit var itunesService: ITunesService

  @Before
  fun setup() {
    retrofit = Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create())
      .client(OkHttpClient.Builder().loggingInterceptor().build())
      .build()
    itunesService = retrofit.create(ITunesService::class.java)
  }

  @Test
  fun testGetMovieByCountry() {
    val map = mapOf("term" to "star", "country" to "au", "media" to "movie")
    runBlocking {
      val request = itunesService.getMovieByCountry(map)
      println(request.body()?.results?.get(0))
      assertNotNull(request.body()?.results)
    }
  }
}