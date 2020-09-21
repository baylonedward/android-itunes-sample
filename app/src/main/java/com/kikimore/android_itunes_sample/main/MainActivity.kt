package com.kikimore.android_itunes_sample.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.main.master.MasterFragmentDirections
import com.kikimore.android_itunes_sample.utils.fetchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

  private val api by lazy { ITunesApi(this) }
  private val viewModel by lazy { fetchViewModel { MainViewModel(api) } }
  private val isTablet by lazy { resources?.getBoolean(R.bool.isTablet) ?: false }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    // fetch data
    viewModel.getTracks()
    // observe selected track
    // if not tablet, navigate to detail fragment on track selected
    if (!isTablet)
      viewModel.onSelect
        .onEach {
          if (it == null) return@onEach
          findNavController(R.id.nav_host_fragment).navigate(MasterFragmentDirections.actionNavigationMasterToDetail())
        }.launchIn(lifecycleScope)
  }
}