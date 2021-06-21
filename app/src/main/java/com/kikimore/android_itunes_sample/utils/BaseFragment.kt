package com.kikimore.android_itunes_sample.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by: ebaylon.
 * Created on: 21/12/2020.
 */
abstract class BaseFragment<FragmentBinding : ViewBinding> : Fragment() {
  private var _binding: FragmentBinding? = null
  protected val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = setBinding(inflater, container)
    return binding.root
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  abstract fun setBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBinding
}