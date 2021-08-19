package com.example.tweetmap.data.models

data class RuleResponseModel(
    var data: List<RuleModel>,
    var meta: Meta

)

data class Meta(
    var sent: String,
    var summary: Summary
)

data class Summary(
    var created: Int,
    var notCreated: Int

)