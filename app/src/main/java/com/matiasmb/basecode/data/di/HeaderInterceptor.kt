package com.matiasmb.basecode.data.di

import com.matiasmb.basecode.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Accept", "application/json")
        builder.addHeader("Authorization", BuildConfig.AUTHORIZATION)
        val request = builder.build()
        return chain.proceed(request)
    }
}
