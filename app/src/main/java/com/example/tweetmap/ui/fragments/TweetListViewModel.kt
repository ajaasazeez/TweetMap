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
import okio.Buffer
import java.nio.charset.Charset
import javax.inject.Inject

@HiltViewModel
class TweetListViewModel @Inject
constructor(private val dataRepository: DataRepositorySource) : ViewModel() {

    private val ruleResponseLiveDataPrivate = MutableLiveData<Resource<RuleResponseModel>>()
    val ruleResponseLiveData: LiveData<Resource<RuleResponseModel>> get() = ruleResponseLiveDataPrivate

    private val streamResponseLiveDataPrivate = MutableLiveData<Resource<StreamResponseModel>>()
    val streamResponseLiveData: LiveData<Resource<StreamResponseModel>> get() = streamResponseLiveDataPrivate


    fun getToken() {
        if (PrefsHelper.read(PrefsHelper.TOKEN, "").isNullOrBlank()) {
            viewModelScope.launch {
                dataRepository.getToken().collect {
                    PrefsHelper.write(PrefsHelper.TOKEN, it.data!!.access_token)

                }

            }
        }
    }

    fun postRules(keyWord:String){
        viewModelScope.launch {
            val ruleModel = AddRuleModel(listOf(RuleModel(value = keyWord,tag = keyWord)))
            dataRepository.postRule(ruleModel).collect {
                ruleResponseLiveDataPrivate.value = it
                streamTweets()
                Log.e("postRuleResponse",Gson().toJson(it))
            }

        }
    }

    fun streamTweets() {
        viewModelScope.launch {
            dataRepository.streamTweets().collect {
                Log.e("viewModelStream",it.data.toString())
//                streamResponseLiveDataPrivate.value = it
                /*  val source = it.data?.source()
                val buffer = Buffer()
                while(!source!!.exhausted()){
                    it.data.source().read(buffer, 8192)
                    val data = buffer.readString(Charset.defaultCharset())
                    Log.e("StreamSuccess",data)
                }
            }*/

            }
        }
    }


}