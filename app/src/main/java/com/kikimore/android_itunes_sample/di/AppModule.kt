package com.kikimore.android_itunes_sample.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kikimore.android_itunes_sample.data.local.ITunesDatabase
import com.kikimore.android_itunes_sample.data.local.TrackDao
import com.kikimore.android_itunes_sample.data.remote.ITunesRemoteDataSource
import com.kikimore.android_itunes_sample.data.remote.ITunesService
import com.kikimore.android_itunes_sample.data.repository.ITunesRepository
import com.kikimore.android_itunes_sample.data.utils.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by: ebaylon.
 * Created on: 19/10/2020.
 */

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

  @Provides
  fun provideGson(): Gson = GsonBuilder().create()

  @Provides
  @Singleton
  fun provideOkHttpClient(loggingInterceptor: LoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://itunes.apple.com")
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(okHttpClient)
      .build()
  }

  @Provides
  fun provideITunesService(retrofit: Retrofit): ITunesService {
    return retrofit.create(ITunesService::class.java)
  }

  @Singleton
  @Provides
  fun provideITunesRemoteDataSource(iTunesService: ITunesService): ITunesRemoteDataSource {
    return ITunesRemoteDataSource(iTunesService)
  }

  @Singleton
  @Provides
  fun provideITunesDatabase(@ApplicationContext context: Context): ITunesDatabase {
    return ITunesDatabase.getDatabase(context)
  }

  @Singleton
  @Provides
  fun provideTrackDao(db: ITunesDatabase): TrackDao {
    return db.trackDao()
  }

  @ExperimentalCoroutinesApi
  @Singleton
  @Provides
  fun provideITunesRepository(
    remoteDataSource: ITunesRemoteDataSource,
    localDataSource: TrackDao
  ): ITunesRepository {
    return ITunesRepository.getInstance(remoteDataSource, localDataSource)
  }
}