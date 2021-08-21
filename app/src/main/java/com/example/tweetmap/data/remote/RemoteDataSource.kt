package com.example.tweetmap.data.remote

import com.example.tweetmap.data.Resource
import com.example.tweetmap.data.models.*
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

internal interface RemoteDataSource {
    suspend fun getTweets(): Resource<TweetResponseModel>
    suspend fun getToken(): Resource<Token>
    suspend fun postRules(addRuleModel:AddRuleModel): Resource<RuleResponseModel>
}
