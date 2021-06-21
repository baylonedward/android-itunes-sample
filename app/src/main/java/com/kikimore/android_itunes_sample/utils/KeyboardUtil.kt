package com.kikimore.android_itunes_sample.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtil {
  /**
   * Show keyboard and focus to given EditText
   *
   * @param context Context
   * @param target  EditText to focus
   */
  fun showKeyboard(context: Context?, target: EditText?) {
    if (context == null || target == null) {
      return
    }

    target.requestFocus()
    val imm = getInputMethodManager(context)

    imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
  }

  /**
   * hide keyboard
   *
   * @param context Context
   * @param target  View that currently has focus
   */
  fun hideKeyboard(context: Context?, target: View?) {
    if (context == null || target == null) {
      return
    }

    val imm = getInputMethodManager(context)
    imm.hideSoftInputFromWindow(target.windowToken, 0)
  }

  private fun getInputMethodManager(context: Context): InputMethodManager {
    return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  }

}