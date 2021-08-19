package com.example.tweetmap.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.tweetmap.data.Resource
import com.example.tweetmap.databinding.FragmentTweetListBinding
import com.example.tweetmap.utils.PrefsHelper
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetListFragment : Fragment() {

    private lateinit var binding: FragmentTweetListBinding
    private val tweetListViewModel: TweetListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTweetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (PrefsHelper.read(PrefsHelper.TOKEN, "").isNullOrBlank())
            tweetListViewModel.getToken()
        tweetListViewModel.streamTweets()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyWord: String?): Boolean {
                keyWord?.let {
                    tweetListViewModel.postRules(it)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        setObservers()

    }

    private fun setObservers() {
        tweetListViewModel.streamResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> Log.e("Loading","true") /*showLoadingView()*/
                is Resource.Success -> Log.e("Success",Gson().toJson(it.data))
                is Resource.DataError -> {
                   /* showDataView(false)
                    status.errorCode?.let { recipesListViewModel.showToastMessage(it) }*/
                }
            }
        })
    }
}