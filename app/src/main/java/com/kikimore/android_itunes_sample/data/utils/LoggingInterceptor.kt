package com.kikimore.android_itunes_sample.data.utils

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by: ebaylon.
 * Created on: 19/10/2020.
 */
class LoggingInterceptor @Inject constructor() : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val chainRequest = chain.request()
    //request
    val requestTime = System.nanoTime()
    println(
      String.format(
        "Sending request %s on %s%n%s",
        chainRequest.url(),
        chain.connection(),
        chainRequest.headers()
      )
    )
    //response
    val response = chain.proceed(chainRequest)
    val responseTime = System.nanoTime()
    println(
      String.format(
        "Received response for %s in %.1fms%n",
        chainRequest.url(),
        (responseTime - requestTime) / 1e6
      )
    )
    //log network response
    val networkResponse = response.networkResponse()
    println(networkResponse)
    return response
  }
}