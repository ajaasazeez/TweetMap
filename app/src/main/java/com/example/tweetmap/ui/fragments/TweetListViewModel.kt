package com.example.tweetmap.ui.fragments

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetmap.data.DataRepositorySource
import com.example.tweetmap.data.Resource
import com.example.tweetmap.data.models.AddRuleModel
import com.example.tweetmap.data.models.RuleModel
import com.example.tweetmap.data.models.RuleResponseModel
import com.example.tweetmap.data.models.TweetResponseModel
import com.example.tweetmap.utils.PrefsHelper
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class TweetListViewModel @Inject
constructor(private val dataRepository: DataRepositorySource) : ViewModel() {

    private val ruleResponseLiveDataPrivate = MutableLiveData<Resource<RuleResponseModel>>()
    val ruleResponseLiveData: LiveData<Resource<RuleResponseModel>> get() = ruleResponseLiveDataPrivate

    private val streamResponseLiveDataPrivate = MutableLiveData<Resource<TweetResponseModel>>()
    val streamResponseLiveData: LiveData<Resource<TweetResponseModel>> get() = streamResponseLiveDataPrivate


    fun getToken() {
        if (PrefsHelper.read(PrefsHelper.TOKEN, "").isNullOrBlank()) {
            viewModelScope.launch {
                dataRepository.getToken().collect {
                    PrefsHelper.write(PrefsHelper.TOKEN, it.data!!.access_token)

                }

            }
        }
    }

    fun postRules(keyWord: String) {
        viewModelScope.launch {
            val ruleModel = AddRuleModel(listOf(RuleModel(value = keyWord, tag = keyWord)))
            dataRepository.postRule(ruleModel).collect {
                ruleResponseLiveDataPrivate.value = it
                streamTweets()
            }

        }
    }

    fun streamTweets() {
        viewModelScope.launch {
            dataRepository.streamTweets().collect {
                streamResponseLiveDataPrivate.value = it
            }
        }
    }

    //for generating random location
    fun getRandomLocation(point: LatLng, radius: Int): LatLng? {
        val randomPoints: MutableList<LatLng> = ArrayList()
        val randomDistances: MutableList<Float> = ArrayList()
        val myLocation = Location("")
        myLocation.latitude = point.latitude
        myLocation.longitude = point.longitude

        //This is to generate 10 random points
        for (i in 0..9) {
            val x0 = point.latitude
            val y0 = point.longitude
            val random = Random()

            // Convert radius from meters to degrees
            val radiusInDegrees = (radius / 111000f).toDouble()
            val u: Double = random.nextDouble()
            val v: Double = random.nextDouble()
            val w = radiusInDegrees * Math.sqrt(u)
            val t = 2 * Math.PI * v
            val x = w * Math.cos(t)
            val y = w * Math.sin(t)

            // Adjust the x-coordinate for the shrinking of the east-west distances
            val new_x = x / Math.cos(y0)
            val foundLatitude = new_x + x0
            val foundLongitude = y + y0
            val randomLatLng = LatLng(foundLatitude, foundLongitude)
            randomPoints.add(randomLatLng)
            val l1 = Location("")
            l1.latitude = randomLatLng.latitude
            l1.longitude = randomLatLng.longitude
            randomDistances.add(l1.distanceTo(myLocation))
        }
        //Get nearest point to the centre
        val indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances))
        return randomPoints[indexOfNearestPointToCentre]
    }


}