package com.kikimore.android_itunes_sample.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.data.entities.Track
import com.kikimore.android_itunes_sample.data.repository.ITunesRepository
import com.kikimore.android_itunes_sample.data.repository.UserSessionRepository
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.main.master.ListAdapter
import com.kikimore.android_itunes_sample.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */
@FlowPreview
@SuppressLint("StaticFieldLeak")
@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel
@Inject constructor(
  private val repository: ITunesRepository,
  private val userSessionRepository: UserSessionRepository,
  @ApplicationContext private val context: Context
) : ViewModel(), ListAdapter.ListStrategy {
  // track list
  private var tracks: List<Track>? = null

  // track list state
  private val _trackListState = MutableStateFlow<Resource<List<Track>>?>(null)
  val trackListState: StateFlow<Resource<List<Track>>?> = _trackListState

  // selected track
  private val _selectedTrack = SingleLiveData<Track?>(null)
  val selectedTrack: LiveData<Track?> = _selectedTrack

  // navigation
  private val _navigation = SingleLiveData<MainActivity.MainNavigation?>(null)
  val navigation: LiveData<MainActivity.MainNavigation?> = _navigation

  // search
  private var searchQuery: String? = userSessionRepository.searchQuery
  private var searchJob: Job? = null

  /**
   * [ListAdapter.ListStrategy] methods
   */

  override fun getListCount(): Int = tracks?.size ?: getDefaultItemCount()

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

  override fun lastVisit(): String? {
    return userSessionRepository.getLastVisit()
  }

  private fun getTrack(position: Int): Track? {
    val finalPosition = if (position > 0) position - getDefaultItemCount() else position
    return tracks?.get(finalPosition)
  }

  private fun getDefaultItemCount(): Int {
    return if (lastVisit() != null) 1 else 0
  }

  /**
   * Search method
   */
  fun searchTracks(query: String?) {
    if (query == null || query.count() < 3) return
    // update last search query
    userSessionRepository.searchQuery = query
    // search
    searchQuery = query
    val country = "au"
    viewModelScope.launch {
      if (searchJob != null) searchJob?.cancelAndJoin()
      searchJob = repository.getMovieByCountry(query, country)
        .onStart { _trackListState.value = Resource.loading() }
        .onEach { _trackListState.value = it }
        .launchIn(viewModelScope)
    }
  }

  /**
   * Set list of tracks
   */
  fun setTracks(tracks: List<Track>?) {
    this.tracks = tracks?.sortedBy { it.trackName }
    if (!this.tracks.isNullOrEmpty()) {
      val previouslySelectedId = userSessionRepository.openedCollectionId
      if (previouslySelectedId != 0) {
        this.tracks?.indexOfFirst { it.id == userSessionRepository.openedCollectionId }?.also {
          onSelect(it + getDefaultItemCount()).invoke()
          // reset opened collection
          userSessionRepository.openedCollectionId = 0
        }
      } else {
        // select first track by default for master detail view
        if (isTabletMode()) {
          onSelect(0).invoke()
        }
      }
    }
  }

  /**
   * Get query string
   */
  fun getQueryString() = searchQuery

  /**
   * Method for checking tablet mode
   */
  fun isTabletMode(): Boolean = context.resources?.getBoolean(R.bool.isTablet) ?: false

  /**
   * Save selected track
   */
  fun saveSelectedTrack() {
    selectedTrack.value?.id?.also {
      userSessionRepository.openedCollectionId = it
    }
  }

  /**
   * Session methods
   */
  fun getLastVisitDateTime() = userSessionRepository.getLastVisit()

  fun setLastVisitDateTime() = userSessionRepository.setLastVisit()

  /**
   * [MainActivity.MainNavigation] Navigation methods
   */
  private fun navigateFromMasterToDetail() {
    _navigation.value = MainActivity.MainNavigation.MasterToDetail
  }

  fun popBackStack() {
    _navigation.value = MainActivity.MainNavigation.PopBackStack
  }
}