package com.example.tweetmap.data.models

import com.google.android.gms.maps.model.LatLng

data class TweetResponseModel(
    val data: TweetDataModel,
    val includes: IncludesModel,
    val matching_rules: List<MatchingRuleModel>
)

data class Users(
    val id: String,
    val name: String,
    val username: String
)

data class IncludesModel(
    val users: List<Users>
)

data class TweetDataModel(
    val author_id: String,
    val created_at: String,
    var geo: LatLng,
    val id: String,
    val text: String
)

data class MatchingRuleModel(
    val id: String,
    val tag: String,
)
