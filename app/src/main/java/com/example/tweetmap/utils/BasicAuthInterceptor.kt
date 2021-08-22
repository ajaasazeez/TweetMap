package com.example.tweetmap.utils

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class BasicAuthInterceptor(user: String, password: String) : Interceptor {
    private val credentials: String = Credentials.basic(user, password)
    private lateinit var token: String

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        token = if (PrefsHelper.read(PrefsHelper.TOKEN, "").isNullOrEmpty())
            credentials
        else
            "Bearer " + PrefsHelper.read(PrefsHelper.TOKEN, "").toString()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", token)
            .build()
        return chain.proceed(authenticatedRequest)
    }
}