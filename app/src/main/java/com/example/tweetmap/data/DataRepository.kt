package com.example.tweetmap.data

import android.util.Log
import com.example.tweetmap.API_KEY
import com.example.tweetmap.API_SECRET_KEY
import com.example.tweetmap.TWITTER_STREAM_URL
import com.example.tweetmap.data.models.AddRuleModel
import com.example.tweetmap.data.models.RuleResponseModel
import com.example.tweetmap.data.models.Token
import com.example.tweetmap.data.models.TweetResponseModel
import com.example.tweetmap.data.remote.RemoteData
import com.example.tweetmap.utils.BasicAuthInterceptor
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import java.nio.charset.Charset
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

    override suspend fun streamTweets(): Flow<Resource<TweetResponseModel>> {
        return flow {
            val client: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(
                BasicAuthInterceptor(API_KEY, API_SECRET_KEY)
            ).build()

            val request: Request = Request.Builder()
                .url(TWITTER_STREAM_URL)
                .method("GET", null)
                .build()
            val response: okhttp3.Response = client.newCall(request).execute()
            val source = response.body?.source()
            val buffer = Buffer()
            while (!source!!.exhausted()) {
                response.body?.source()?.read(buffer, 8192)
                val data = buffer.readString(Charset.defaultCharset())
                try {
                    val tweetResponseModel: TweetResponseModel = Gson().fromJson(data, TweetResponseModel::class.java)
                    emit(Resource.Success(tweetResponseModel ))
                }
                catch (e:Exception){
                    Log.e("jsonException", data)
                }



            }
        }.flowOn(ioDispatcher)

    }
}