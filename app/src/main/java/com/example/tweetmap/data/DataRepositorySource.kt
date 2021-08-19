package com.example.tweetmap.data

import com.example.tweetmap.data.models.TweetResponseModel
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun getTweets(): Flow<Resource<TweetResponseModel>>
}
