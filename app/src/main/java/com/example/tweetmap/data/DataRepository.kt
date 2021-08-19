package com.example.tweetmap.data

import com.example.tweetmap.data.models.*
import com.example.tweetmap.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepository @Inject constructor(
    private val remoteRepository: RemoteData,
    private val ioDispatcher: CoroutineContext
) : DataRepositorySource {
    override suspend fun getTweets(): Flow<Resource<TweetResponseModel>> {
        return flow {
            emit(remoteRepository.getTweets())
        }.flowOn(ioDispatcher)
    }

    override suspend fun getToken(): Flow<Resource<Token>> {
        return flow {
            emit(remoteRepository.getToken())
        }.flowOn(ioDispatcher)
    }

    override suspend fun postRule(addRuleModel: AddRuleModel): Flow<Resource<RuleResponseModel>> {
        return flow {
            emit(remoteRepository.postRules(addRuleModel))
        }.flowOn(ioDispatcher)
    }

    override suspend fun streamTweets(): Flow<Resource<StreamResponseModel>> {
        return flow {
            emit(remoteRepository.streamTweets())
        }.flowOn(ioDispatcher)
    }
}