package com.kikimore.android_itunes_sample.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.databinding.ActivityMainBinding
import com.kikimore.android_itunes_sample.main.master.MasterFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()
  private val isTablet by lazy { resources?.getBoolean(R.bool.isTablet) ?: false }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
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