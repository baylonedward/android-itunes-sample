package com.kikimore.android_itunes_sample.data.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 *
 * 1. Loading State
 * 2. Return observable data from local source
 * 3. Invoke network call
 * 4. Save network call response to database
 * 5. Network call method if you want to do something else of the network response.
 */

@ExperimentalCoroutinesApi
fun <T, A> performGetOperation(
  databaseQuery: () -> Flow<T>,
  networkCall: suspend () -> Resource<A>,
  saveCallResult: suspend (A) -> Unit,
  networkCallBack: (suspend (A) -> Unit)? = null
): Flow<Resource<T>> = channelFlow<Resource<T>> {
  // 1. Loading State
  send(Resource.loading())
  // Return observable data
  launch {
    databaseQuery.invoke().map { Resource.success(it) }.collect {
      send(it)
    }
  }
  //3. Invoke network call
  val responseStatus = networkCall.invoke()
  when (responseStatus.status) {
    Resource.Status.SUCCESS -> {
      responseStatus.data?.also {
        // 4. Save network call response to database
        saveCallResult(it)
        // 5. additional callback method for response
        networkCallBack?.invoke(it)
      }
    }
    Resource.Status.ERROR -> send(Resource.error(responseStatus.message!!))
    else -> {
    }
  }
}.flowOn(Dispatchers.IO)