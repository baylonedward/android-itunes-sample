package com.kikimore.android_itunes_sample.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.data.entities.Track
import com.kikimore.android_itunes_sample.data.repository.ITunesRepository
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.main.master.ListAdapter
import com.kikimore.android_itunes_sample.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */
@SuppressLint("StaticFieldLeak")
@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel
@Inject constructor(
  private val repository: ITunesRepository,
  @ApplicationContext private val context: Context
) : ViewModel(), ListAdapter.ListStrategy {
  private var tracks: List<Track>? = null
  private val _trackListState = MutableStateFlow<Resource<List<Track>>?>(null)
  val trackListState: StateFlow<Resource<List<Track>>?> = _trackListState
  private val _selectedTrack = MutableStateFlow<Track?>(null)
  val selectedTrack: StateFlow<Track?> = _selectedTrack
  private val _navigation = SingleLiveData<MainActivity.MainNavigation?>(null)
  val navigation: LiveData<MainActivity.MainNavigation?> = _navigation

  /**
   * [ListAdapter.ListStrategy] methods
   */

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
    _selectedTrack.value = getTrack(position)
    navigateFromMasterToDetail()
  }

  private fun getTrack(position: Int) = tracks?.get(position)

  /**
   * Get Tracks
   */
  fun getTracks() {
    val query = "star"
    val country = "au"
    repository.getMovieByCountry(query, country)
      .distinctUntilChanged()
      .catch { _trackListState.value = Resource.error(it.message!!) }
      .onEach { resource ->
        _trackListState.value = resource
        resource.data?.also { tracks ->
          this.tracks = tracks.sortedBy { it.trackName }
          // select first track by default for master detail view
          if (!this.tracks.isNullOrEmpty()) {
            _selectedTrack.value = selectedTrack.value ?: this.tracks?.get(0)
          }
        }
      }.launchIn(viewModelScope)
  }

  /**
   * Method for checking tablet mode
   */
  fun isTabletMode(): Boolean = context.resources?.getBoolean(R.bool.isTablet) ?: false


  /**
   * [MainActivity.MainNavigation] Navigation methods
   */
  private fun navigateFromMasterToDetail() {
    _navigation.value = MainActivity.MainNavigation.MasterToDetail
  }
}