package com.example.tweetmap.data

import com.example.tweetmap.data.models.*
import kotlinx.coroutines.flow.Flow


interface DataRepositorySource {
    suspend fun getTweets(): Flow<Resource<TweetResponseModel>>
    suspend fun getToken(): Flow<Resource<Token>>
    suspend fun postRule(addRuleModel:AddRuleModel): Flow<Resource<RuleResponseModel>>
    suspend fun streamTweets(): Flow<Resource<StreamResponseModel>>
}
