package com.kikimore.android_itunes_sample.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by: ebaylon.
 * Created on: 15/09/2020.
 */
@Entity(tableName = "tracks")
data class Track(
  @PrimaryKey
  @SerializedName("collectionId") val id: Int,
  val trackName: String,
  @SerializedName("artworkUrl100") val artWork: String,
  @SerializedName("trackPrice") val price: String?,
  @SerializedName("primaryGenreName") val genre: String,
  val currency: String,
  val longDescription: String,
)