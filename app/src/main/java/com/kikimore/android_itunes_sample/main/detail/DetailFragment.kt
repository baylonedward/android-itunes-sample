package com.kikimore.android_itunes_sample.main.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.data.entities.Track
import com.kikimore.android_itunes_sample.databinding.FragmentDetailBinding
import com.kikimore.android_itunes_sample.main.MainViewModel
import com.kikimore.android_itunes_sample.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
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
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

  private val viewModel: MainViewModel by activityViewModels()

  override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDetailBinding {
    return FragmentDetailBinding.inflate(layoutInflater)
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
      .into(binding.imageView)
    // track name
    binding.trackNameTextView.text = track.trackName
    // genre
    binding.genreTextView.text = track.genre
    //price
    val currency = Currency.getInstance(track.currency).symbol
    binding.priceTextView.text = "$currency ${track.price}"
    // description
    binding.longDescTextView.text = track.longDescription
  }
}