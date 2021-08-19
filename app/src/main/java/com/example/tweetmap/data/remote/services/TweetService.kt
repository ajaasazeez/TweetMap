package com.example.tweetmap.data.remote.services

import com.example.tweetmap.data.models.TweetResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface TweetService {
    @GET("/2/tweets/search/stream")
    suspend fun getTweets(): Response<List<TweetResponseModel>>
}