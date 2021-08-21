package com.example.tweetmap.data.models

data class TweetResponseModel(
    val data:TweetDataModel,
    val matching_rules:List<MatchingRuleModel>
)

data class TweetDataModel(
    val author_id:String,
    val created_at:String,
    val geo:Geo,
    val id:String,
    val text:String
)

data class Geo(
    val latitude:Long,
    val longitude:Long,
)

data class MatchingRuleModel(
   val id:String,
   val tag:String,
)
