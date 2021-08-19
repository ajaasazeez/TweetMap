package com.example.tweetmap.data.remote

import com.example.tweetmap.data.Resource
import com.example.tweetmap.data.models.TweetResponseModel

internal interface RemoteDataSource {
    suspend fun getTweets(): Resource<TweetResponseModel>
}
