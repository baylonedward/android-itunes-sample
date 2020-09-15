package com.kikimore.android_itunes_sample

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kikimore.android_itunes_sample.data.local.ITunesDatabase
import com.kikimore.android_itunes_sample.data.remote.ITunesRemoteDataSource
import com.kikimore.android_itunes_sample.data.remote.ITunesService
import com.kikimore.android_itunes_sample.data.repository.ITunesRepository
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.data.utils.loggingInterceptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
  private lateinit var testSetup: TestSetup

  @Before
  fun setup() {
    testSetup = TestSetup()
  }

  @Test
  fun testGetMovieByCountry() {
    val query = "star"
    runBlocking {
      launch {
        testSetup.iTunesRepository.getMovieByCountry(query).collect {
          when (it.status) {
            Resource.Status.LOADING -> {
              Assert.assertNull(it.data)
            }
            Resource.Status.SUCCESS -> {
              Assert.assertNotNull(it.data)
              if (it.data?.count() != 0) {
                val track1 = it.data?.get(0)
                Assert.assertNotNull(track1?.trackName)
                cancel()
              }
            }
            Resource.Status.ERROR -> {
              Assert.assertNotNull(it.message)
              cancel()
            }
          }
        }
      }
    }
  }
}

@ExperimentalCoroutinesApi
class TestSetup {
  private val context = ApplicationProvider.getApplicationContext<Context>()
  private val db = Room.inMemoryDatabaseBuilder(context, ITunesDatabase::class.java).build()
  private val baseUrl = "https://itunes.apple.com"
  private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder().loggingInterceptor().build())
    .build()
  private val iTunesService = retrofit.create(ITunesService::class.java)
  private val iTunesRemoteDataSource = ITunesRemoteDataSource(iTunesService)

  val iTunesRepository = ITunesRepository(
    localDataSource = db.trackDao(),
    remoteDataSource = iTunesRemoteDataSource
  )
}