package com.kikimore.android_itunes_sample.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kikimore.android_itunes_sample.data.entities.Track

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 *
 * Database class for local storage
 */
@Database(
  entities = [Track::class],
  version = 4,
  exportSchema = false
)
abstract class ITunesDatabase : RoomDatabase() {
  abstract fun trackDao(): TrackDao

  companion object {
    @Volatile
    private var instance: ITunesDatabase? = null

    fun getDatabase(context: Context): ITunesDatabase =
      instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

    private fun buildDatabase(appContext: Context) =
      Room.databaseBuilder(appContext, ITunesDatabase::class.java, "itunes")
        .fallbackToDestructiveMigration()
        .build()
  }
}