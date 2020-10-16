package com.kikimore.android_itunes_sample.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kikimore.android_itunes_sample.data.entities.Track
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.main.master.ListStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */
@ExperimentalCoroutinesApi
class MainViewModel(
  private val api: ITunesApi
) : ViewModel(), ListStrategy {

  private var tracks: List<Track>? = null
  val trackListState = MutableStateFlow<Resource<List<Track>>?>(null)
  var selectedTrack = MutableStateFlow<Track?>(null)
  var onSelect = MutableStateFlow<Boolean?>(null)

  fun getTracks() {
    val query = "star"
    val country = "au"
    api.iTunesRepository.getMovieByCountry(query, country)
      .distinctUntilChanged()
      .catch { trackListState.value = Resource.error(it.message!!) }
      .onEach {
        trackListState.value = it
        it.data?.also { tracks ->
          this.tracks = tracks.sortedBy { it.trackName }
          // select first track by default for master detail view
          if (!this.tracks.isNullOrEmpty()) {
            selectedTrack.value = this.tracks?.get(0)
          }
        }
      }.launchIn(viewModelScope)
  }

  private fun getTrack(position: Int) = tracks?.get(position)

  override fun getListCount(): Int = tracks?.size ?: 0

  override fun getImageUrl(position: Int): String? = getTrack(position)?.artWork

  override fun getTrackName(position: Int): String = getTrack(position)?.trackName ?: ""

  override fun getGenre(position: Int): String = "Genre: ${getTrack(position)?.genre}"

  override fun getPrice(position: Int): String {
    val price = getTrack(position)?.price
    val currencyCode = getTrack(position)?.currency
    val currency = currencyCode?.let {
      Currency.getInstance(it).symbol
    }
    return "$currency $price"
  }

  override fun onSelect(position: Int): () -> Unit = {
    selectedTrack.value = getTrack(position)
    onSelect.value = true
    onSelect.value = null
  }
}