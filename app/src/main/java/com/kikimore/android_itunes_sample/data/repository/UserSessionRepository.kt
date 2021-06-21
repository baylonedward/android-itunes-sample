package com.kikimore.android_itunes_sample.data.repository

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by: ebaylon.
 * Created on: 22/06/2021.
 */
class UserSessionRepository private constructor(private val sharedPreferences: SharedPreferences) {

  private val editor = sharedPreferences.edit()

  private var lastVisitDateTime: String?
    get() = sharedPreferences.getString(LAST_VISIT_KEY, null)
    set(value) = editor.putString(LAST_VISIT_KEY, value).apply()

  private fun Date.toStringFormat(
    format: String = DATE_FORMAT,
    locale: Locale = Locale.getDefault()
  ): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
  }

  fun getLastVisit() = lastVisitDateTime

  fun setLastVisit() {
    val date = Calendar.getInstance().time.toStringFormat()
    lastVisitDateTime = date
  }

  companion object {
    const val PREFERENCE_NAME: String = "iTunesSamplePref"
    private const val LAST_VISIT_KEY = "idKey"
    private const val DATE_FORMAT = "yyyy/MM/dd HH:mm:ss"

    @Volatile
    private var instance: UserSessionRepository? = null
    fun getInstance(sharedPreferences: SharedPreferences) = instance ?: synchronized(this) {
      println("Session Repository is null, creating new instance.")
      UserSessionRepository(sharedPreferences).also { instance = it }
    }
  }
}