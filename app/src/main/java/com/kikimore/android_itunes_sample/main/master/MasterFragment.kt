package com.kikimore.android_itunes_sample.main.master

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.databinding.FragmentMasterBinding
import com.kikimore.android_itunes_sample.main.MainViewModel
import com.kikimore.android_itunes_sample.utils.BaseFragment
import com.kikimore.android_itunes_sample.utils.KeyboardUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MasterFragment : BaseFragment<FragmentMasterBinding>() {
  private val viewModel: MainViewModel by activityViewModels()
  private val listAdapter by lazy { ListAdapter(viewModel) }

  override fun setBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMasterBinding {
    return FragmentMasterBinding.inflate(layoutInflater)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.searchTracks(getString(R.string.star))
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setListView()
    setObservers()
    setSearchView()
    if (viewModel.isTabletMode()) displayDetailLayout() // display detail fragment on Tablet mode
  }

  private fun setSearchView() {
    // set search edit text
    binding.searchBarEditText.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN) search()
      true
    }
    // set search icon
    binding.searchBarSearchImageView.setOnClickListener { search() }
    // set default search query if none
    if (binding.searchBarEditText.text.isEmpty()) binding.searchBarEditText.setText(getString(R.string.star))
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
      println("Track List: ${state?.data?.size}")
      if (state == null) return@onEach
      when (state.status) {
        Resource.Status.SUCCESS -> {
          viewModel.setTracks(state.data)
          listAdapter.notifyDataSetChanged()
        }
        Resource.Status.LOADING -> {
          //toast("Loading")
        }
        Resource.Status.ERROR -> {
          state.message?.also { toast(it) }
        }
      }
      listAdapter.notifyDataSetChanged()
    }.launchIn(lifecycleScope)
  }

  private fun search() {
    binding.searchBarEditText.apply {
      viewModel.searchTracks(this.text.toString())
      clearFocus()
      KeyboardUtil.hideKeyboard(this@MasterFragment.context, this)
    }
  }

  private fun toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)?.show()
  }
}