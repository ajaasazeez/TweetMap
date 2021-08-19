package com.example.tweetmap.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetmap.data.DataRepositorySource
import com.example.tweetmap.data.Resource
import com.example.tweetmap.data.models.*
import com.example.tweetmap.utils.PrefsHelper
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TweetListViewModel @Inject
constructor(private val dataRepository: DataRepositorySource) : ViewModel() {

    private val tweetsLiveDataPrivate = MutableLiveData<Resource<TweetResponseModel>>()
    val tweetsLiveData: LiveData<Resource<TweetResponseModel>> get() = tweetsLiveDataPrivate

    private val ruleResponseLiveDataPrivate = MutableLiveData<Resource<RuleResponseModel>>()
    val ruleResponseLiveData: LiveData<Resource<RuleResponseModel>> get() = ruleResponseLiveDataPrivate

    private val streamResponseLiveDataPrivate = MutableLiveData<Resource<StreamResponseModel>>()
    val streamResponseLiveData: LiveData<Resource<StreamResponseModel>> get() = streamResponseLiveDataPrivate

    fun getTweets() {
        viewModelScope.launch {
            tweetsLiveDataPrivate.value = Resource.Loading()
            dataRepository.getTweets().collect {
                tweetsLiveDataPrivate.value = it
            }

        }
    }

    fun getToken() {
        viewModelScope.launch {

            dataRepository.getToken().collect {
                Log.e("Token",it.toString())
                PrefsHelper.write(PrefsHelper.TOKEN, it.data!!.access_token)
                Log.e("TokenFromPref", PrefsHelper.read(PrefsHelper.TOKEN,"").toString())
            }

        }
    }

    fun postRules(keyWord:String){
        viewModelScope.launch {
            val ruleModel = AddRuleModel(listOf(RuleModel(value = keyWord,tag = keyWord)))
            dataRepository.postRule(ruleModel).collect {
                ruleResponseLiveDataPrivate.value = it

                Log.e("postRuleResponse",Gson().toJson(it))
            }

        }
    }

    fun streamTweets(){
        viewModelScope.launch {
            dataRepository.streamTweets().collect {
                streamResponseLiveDataPrivate.value = it
            }
        }
    }


}