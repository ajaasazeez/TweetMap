package com.example.tweetmap.data.remote

import com.example.tweetmap.CLIENT_CREDENTIALS
import com.example.tweetmap.data.Error.NETWORK_ERROR
import com.example.tweetmap.data.Error.NO_INTERNET_CONNECTION
import com.example.tweetmap.data.Resource
import com.example.tweetmap.data.models.AddRuleModel
import com.example.tweetmap.data.models.RuleResponseModel
import com.example.tweetmap.data.models.Token
import com.example.tweetmap.data.models.TweetResponseModel
import com.example.tweetmap.data.remote.services.TweetService
import com.example.tweetmap.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteData @Inject
constructor(
    private val api: TweetService,
    private val networkConnectivity: NetworkConnectivity
) : RemoteDataSource {

    override suspend fun getToken(): Resource<Token> {
        return when (val response = processCall { api.getToken(CLIENT_CREDENTIALS) }) {
            is Token -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    override suspend fun postRules(addRuleModel: AddRuleModel): Resource<RuleResponseModel> {
        return when (val response = processCall { api.postRule(addRuleModel) }) {
            is RuleResponseModel -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }

}