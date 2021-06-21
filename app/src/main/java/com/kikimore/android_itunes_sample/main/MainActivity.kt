package com.kikimore.android_itunes_sample.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.databinding.ActivityMainBinding
import com.kikimore.android_itunes_sample.main.master.MasterFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()
  private val navController by lazy { findNavController(R.id.nav_host_fragment) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // observe selected track
    // if not tablet, navigate to detail fragment on track selected
    if (!viewModel.isTabletMode())
      viewModel.navigation.observe(this) { navigation ->
        if (navigation == null) return@observe
        if (navigation == MainNavigation.PopBackStack) navController.popBackStack()
        else navigation.navDirection?.also { navController.navigate(it) }
      }

    // show last visit date
    println("Last: ${viewModel.getLastVisitDateTime()}")
    viewModel.getLastVisitDateTime()?.also {
      toast(getString(R.string.last_visited, it))
    }
  }

  override fun onPause() {
    super.onPause()
    viewModel.setLastVisitDateTime()
  }

  private fun toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG)?.show()
  }

  /**
   * class to contain all [NavDirections] of main navigation graph
   */
  sealed class MainNavigation(val navDirection: NavDirections? = null) {
    object PopBackStack : MainNavigation()

    object MasterToDetail :
      MainNavigation(MasterFragmentDirections.actionNavigationMasterToDetail())
  }
}