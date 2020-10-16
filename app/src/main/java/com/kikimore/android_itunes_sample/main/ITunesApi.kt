package com.kikimore.android_itunes_sample.main

import android.app.Application
import android.content.Context
import com.kikimore.android_itunes_sample.data.local.ITunesDatabase
import com.kikimore.android_itunes_sample.data.remote.ITunesRemoteDataSource
import com.kikimore.android_itunes_sample.data.remote.ITunesService
import com.kikimore.android_itunes_sample.data.repository.ITunesRepository
import com.kikimore.android_itunes_sample.data.utils.loggingInterceptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 *
 * Singleton class to access data api
 */
class ITunesApi private constructor(context: Context) {
  private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder().loggingInterceptor().build())
    .build()
  private val iTunesService = retrofit.create(ITunesService::class.java)
  private val remoteDataSource = ITunesRemoteDataSource(iTunesService)
  private val db = ITunesDatabase.getDatabase(context)

  val iTunesRepository = ITunesRepository.getInstance(
    remoteDataSource = remoteDataSource,
    localDataSource = db.trackDao()
  )

  //singleton
  companion object {
    private const val baseUrl = "https://itunes.apple.com"

    @Volatile
    private var instance: ITunesApi? = null
    fun getInstance(application: Application) = instance ?: synchronized(this) {
      ITunesApi(application).also { instance = it }
    }
  }
}