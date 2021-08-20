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
import com.example.tweetmap.R
import com.example.tweetmap.data.Resource
import com.example.tweetmap.databinding.FragmentTweetListBinding
import com.example.tweetmap.utils.PrefsHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TweetListFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}