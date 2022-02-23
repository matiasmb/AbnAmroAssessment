package com.matiasmb.basecode.util

sealed class Resource<T> (val data: T? = null, val error: Throwable? = null) {

    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(throwable: Throwable? = null, data: T? = null): Resource<T>(data, throwable)
}
