package com.kikimore.android_itunes_sample.main.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.kikimore.android_itunes_sample.R
import com.kikimore.android_itunes_sample.data.utils.Resource
import com.kikimore.android_itunes_sample.main.ITunesApi
import com.kikimore.android_itunes_sample.main.MainViewModel
import com.kikimore.android_itunes_sample.utils.fetchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by: ebaylon.
 * Created on: 16/09/2020.
 */
@ExperimentalCoroutinesApi
class MasterFragment : Fragment() {

  private val api by lazy { ITunesApi(requireActivity().applicationContext) }
  private val viewModel by lazy { requireActivity().fetchViewModel { MainViewModel(api) } }
  private val listAdapter by lazy { ListAdapter(viewModel) }
  private val isTablet by lazy { context?.resources?.getBoolean(R.bool.isTablet) ?: false }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // inflate layout according to screen type
    return inflater.inflate(R.layout.fragment_master, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setListView(view.findViewById(R.id.trackListView))
    setObservers()
    if (isTablet) displayDetailLayout() // display detail fragment on Tablet mode
  }

  private fun setListView(recyclerView: RecyclerView) {
    recyclerView.adapter = listAdapter
  }

  private fun displayDetailLayout() {
    val navHostFragment =
      childFragmentManager.findFragmentById(R.id.detail_nav_container) as NavHostFragment
    navHostFragment.navController.navigate(R.id.navigation_detail_fragment)
  }

  private fun setObservers() {
    // list state
    viewModel.trackListState.onEach {
      if (it == null) return@onEach
      when (it.status) {
        Resource.Status.SUCCESS -> {
          listAdapter.notifyDataSetChanged()
        }
        Resource.Status.LOADING -> {
          toast("Loading")
        }
        Resource.Status.ERROR -> {
          toast(it.message!!)
        }
      }
    }.launchIn(lifecycleScope)
  }

  private fun toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)?.show()
  }
}