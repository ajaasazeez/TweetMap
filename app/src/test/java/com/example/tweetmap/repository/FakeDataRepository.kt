package com.example.tweetmap.repository

import com.example.tweetmap.data.DataRepositorySource
import com.example.tweetmap.data.Resource
import com.example.tweetmap.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDataRepository : DataRepositorySource {

    private var shouldReturnNetworkError = true

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }


    override suspend fun getToken(): Flow<Resource<Token>> {
        return flow {
            if (shouldReturnNetworkError) {
                return@flow emit(Resource.DataError(errorCode = 101))
            } else {
                return@flow emit(
                    Resource.Success(
                        Token(
                            token_type = "bearer",
                            access_token = "fakeAccessCode"
                        )
                    )
                )
            }
        }
    }

    override suspend fun postRule(addRuleModel: AddRuleModel): Flow<Resource<RuleResponseModel>> {
        return flow {
            if (shouldReturnNetworkError) {
                return@flow emit(Resource.DataError(errorCode = 101))
            } else {
                return@flow emit(
                    Resource.Success(
                        RuleResponseModel(
                            data = listOf<RuleModel>(),
                            meta = Meta("sent", Summary(1,0))
                        )
                    )
                )
            }
        }
    }

    override suspend fun streamTweets(): Flow<Resource<TweetResponseModel>> {
        TODO("Not yet implemented")
    }
}