package com.example.tweetmap.data.remote.services

import com.example.tweetmap.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface TweetService {
    @GET("2/tweets/search/stream")
    suspend fun getTweets(): Response<List<TweetResponseModel>>

    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getToken(@Field("grant_type") grantType: String): Response<Token>


    @POST("2/tweets/search/stream/rules")
    suspend fun postRule(@Body ruleModel: AddRuleModel): Response<RuleResponseModel>

    @GET("2/tweets/search/stream")
    suspend fun streamTweets(): Response<StreamResponseModel>
}