package com.example.tweetmap.data

import com.example.tweetmap.data.models.TweetResponseModel
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
}