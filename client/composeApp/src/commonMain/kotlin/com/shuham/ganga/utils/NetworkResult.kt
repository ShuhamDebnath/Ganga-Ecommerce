package com.shuham.ganga.utils

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {

    // Success always has Data
    class Success<T>(data: T) : NetworkResult<T>(data)

    // Error always has a Message, and sometimes cached Data
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)

    // Loading state
    class Loading<T> : NetworkResult<T>()
}