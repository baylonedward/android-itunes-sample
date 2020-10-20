package com.kikimore.android_itunes_sample.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.data.entities.Track
import com.kikimore.android_itunes_sample.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailFragment : Fragment() {

  private val viewModel: MainViewModel by activityViewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_detail, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // observe selected track
    viewModel.selectedTrack.onEach {
      if (it == null) return@onEach
      setDetails(it, view)
    }.launchIn(lifecycleScope)
  }

  private fun setDetails(track: Track, view: View) {
    // image view
    Glide.with(view)
      .load(track.artWork)
      .centerInside()
      .placeholder(R.drawable.ic_movie_placeholder)
      .into(view.imageView)
    // track name
    view.trackNameTextView.text = track.trackName
    // genre
    view.genreTextView.text = track.genre
    //price
    val currency = Currency.getInstance(track.currency).symbol
    view.priceTextView.text = " - $currency ${track.price}"
    // description
    view.longDescTextView.text = track.longDescription
  }
}