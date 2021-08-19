package com.example.tweetmap.data.models

data class StreamResponseModel(
    val data:TweetModel
)

data class TweetModel(
    val id:String,
    val text:String
)


