package com.kikimore.android_itunes_sample.data.remote

import com.kikimore.android_itunes_sample.data.utils.Resource
import retrofit2.Response

/**
 * Created by: ebaylon.
 * Created on: 23/07/2020.
 *
 * Abstract class to wrapped network response in a single helper class (Resource)
 */
abstract class BaseDataResource {
  protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
    try {
      val response = call()
      if (response.isSuccessful) {
        val body = response.body()
        if (body != null) return Resource.success(body)
      }
      return error(" ${response.code()} ${response.message()}")
    } catch (e: Exception) {
      return error(e.message ?: e.toString())
    }
  }

  private fun <T> error(message: String): Resource<T> {
    return Resource.error("Network call has failed for a following reason: $message")
  }
}