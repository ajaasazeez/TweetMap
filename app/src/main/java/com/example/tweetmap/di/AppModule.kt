package com.example.tweetmap.di

import android.content.Context
import com.example.tweetmap.API_KEY
import com.example.tweetmap.API_SECRET_KEY
import com.example.tweetmap.BASE_URL
import com.example.tweetmap.BuildConfig
import com.example.tweetmap.data.remote.services.TweetService
import com.example.tweetmap.utils.BasicAuthInterceptor
import com.example.tweetmap.utils.Network
import com.example.tweetmap.utils.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Provides
    @Singleton
    fun provideLogger(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
        }
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor(logger)
        okHttpBuilder.connectTimeout(100, TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(100, TimeUnit.SECONDS)
        okHttpBuilder.addInterceptor(BasicAuthInterceptor(API_KEY, API_SECRET_KEY))
        return okHttpBuilder.build()
    }


    @Provides
    @Singleton
    fun providesTwitterApi(client: OkHttpClient): TweetService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(TweetService::class.java)
    }
}