package com.example.tweetmap

const val BASE_URL =  "https://api.twitter.com/"
const val API_KEY =  "2CqQHeK3AWV1cQa4IPusr5I3y"
const val API_SECRET_KEY =  "0w7HEluNyzE6z6N6q3EmCC3HdKl75g0veZ0M1QEQlMhnfViy7b"
const val CLIENT_CREDENTIALS =  "client_credentials"
const val TWITTER_STREAM_URL =  "https://api.twitter.com/2/tweets/search/stream?tweet.fields=created_at&expansions=author_id,geo.place_id&user.fields=created_at&place.fields=contained_within,country,country_code,full_name,geo,id,name,place_type"