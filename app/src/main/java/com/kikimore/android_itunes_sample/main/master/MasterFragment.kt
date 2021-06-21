package com.kikimore.android_itunes_sample.main.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.databinding.FragmentMasterBinding
import com.kikimore.android_itunes_sample.main.MainViewModel
import com.kikimore.android_itunes_sample.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MasterFragment : BaseFragment<FragmentMasterBinding>() {

  private val viewModel: MainViewModel by activityViewModels()
  private val listAdapter by lazy { ListAdapter(viewModel) }
  private val isTablet by lazy { context?.resources?.getBoolean(R.bool.isTablet) ?: false }

  override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMasterBinding {
    return FragmentMasterBinding.inflate(layoutInflater)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // fetch data
    viewModel.getTracks()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setListView()
    setObservers()
    if (isTablet) displayDetailLayout() // display detail fragment on Tablet mode
  }

  private fun setListView() {
    binding.trackListView.adapter = listAdapter
  }

  private fun displayDetailLayout() {
    val navHostFragment =
      childFragmentManager.findFragmentById(R.id.detail_nav_container) as NavHostFragment
    navHostFragment.navController.navigate(R.id.navigation_detail_fragment)
  }

  private fun setObservers() {
    // list state
    viewModel.trackListState.onEach { state ->
      if (state == null) return@onEach
      when (state.status) {
        Resource.Status.SUCCESS -> {
          listAdapter.notifyDataSetChanged()
        }
        Resource.Status.LOADING -> {
          toast("Loading")
        }
        Resource.Status.ERROR -> {
          state.message?.also { toast(it) }
        }
      }
    }.launchIn(lifecycleScope)
  }

  private fun toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)?.show()
  }
}