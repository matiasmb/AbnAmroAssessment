package com.matiasmb.basecode.data.service

sealed class ResponseType<out T> {
    data class Success<out T>(val data: T) : ResponseType<T>()
    object Failure: ResponseType<Nothing>()
}
