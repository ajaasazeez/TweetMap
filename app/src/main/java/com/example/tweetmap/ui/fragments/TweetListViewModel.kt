package com.example.tweetmap.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetmap.data.DataRepositorySource
import com.example.tweetmap.data.Resource
import com.example.tweetmap.data.models.TweetResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TweetListViewModel @Inject
constructor(private val dataRepository: DataRepositorySource) : ViewModel() {

    val tweetsLiveDataPrivate = MutableLiveData<Resource<TweetResponseModel>>()
    val tweetsLiveData: LiveData<Resource<TweetResponseModel>> get() = tweetsLiveDataPrivate

    fun getTweets() {
        viewModelScope.launch {
            tweetsLiveDataPrivate.value = Resource.Loading()
            dataRepository.getTweets().collect {
                tweetsLiveDataPrivate.value = it
            }

        }
    }
}