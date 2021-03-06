package com.example.tweetmap.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tweetmap.data.Resource
import com.example.tweetmap.databinding.FragmentTweetListBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class TweetListFragment : Fragment() {
    private lateinit var currentLocation: LatLng
    private lateinit var googleMap: GoogleMap
    private val RADIUS = 50000
    private lateinit var binding: FragmentTweetListBinding
    private val tweetListViewModel: TweetListViewModel by viewModels()
    private lateinit var locationManager: LocationManager
    private var markerList: MutableList<Marker> = ArrayList()
    private lateinit var searchKey: String
    private val job = startRepeatingJob()
    private var markerLifeSpan = 30
    private val locationListener = LocationListener {
        moveMapToCurrentLocation(it)
    }

    @SuppressLint("MissingPermission")
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                googleMap.isMyLocationEnabled = true
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 90 * 1000,
                    10.0.toFloat(), locationListener
                )
            }
        }

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
        binding.map.onCreate(savedInstanceState)
        locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setUpGoogleMap()
        tweetListViewModel.getToken()
        setListeners()
        setObservers()
        job.start()

    }

    private fun setListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyWord: String?): Boolean {
                keyWord?.let {
                    googleMap.clear()
                    searchKey = keyWord
                    tweetListViewModel.postRules(it)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        binding.btnSet.setOnClickListener {
            markerLifeSpan = binding.etTimer.text.toString().toInt()
        }
    }

    private fun setObservers() {
        tweetListViewModel.streamResponseLiveData.observe(viewLifecycleOwner, { it ->
            when (it) {
                is Resource.Loading -> Log.v("Loading", "true") /*showLoadingView()*/
                is Resource.Success -> {
                    val tweetModel = it.data
                    tweetModel?.let {
                        //set fake location since twitter api doesn't give location
                        tweetModel.data?.let {
                            it.geo = tweetListViewModel.getRandomLocation(
                                LatLng(
                                    currentLocation.latitude,
                                    currentLocation.longitude
                                ), RADIUS
                            )!!
                            if (tweetModel.matching_rules[0].tag == searchKey) {
                                val marker = googleMap.addMarker(
                                    MarkerOptions().position(it.geo)
                                        .title(tweetModel.includes.users[0].username)
                                        .snippet(tweetModel.data.text)
                                )
                                marker?.let {
                                    it.tag = System.currentTimeMillis() / 1000
                                    markerList.add(it)
                                }
                            }
                        }
                    }


                }
                is Resource.DataError -> {

                }
            }
        })
    }

    private fun setUpGoogleMap() {
        binding.map.onResume()
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.map.getMapAsync { mMap ->
            googleMap = mMap
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            } else {
                googleMap.isMyLocationEnabled = true
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 90 * 1000,
                    10.0.toFloat(), locationListener
                )
                googleMap.setOnInfoWindowClickListener {
                    val direction = TweetListFragmentDirections.actionTweetListFragmentToTweetDetailFragment(it.snippet.toString())
                    findNavController().navigate(direction)
                }
            }
        }


    }

    private fun moveMapToCurrentLocation(location: Location) {
        currentLocation = LatLng(location.latitude, location.longitude)
        val cameraPosition = CameraPosition.Builder().target(currentLocation).zoom(12f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    // to remove markers after customizable lifespan
    private fun startRepeatingJob(): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                if (markerList.isNotEmpty()) {
                    markerList.forEach { marker ->
                        val currentTimeStamp = System.currentTimeMillis() / 1000
                        activity?.runOnUiThread {
                            marker.tag?.let {
                                if (currentTimeStamp.minus(
                                        it.toString().toLong()
                                    ) > markerLifeSpan
                                ) {
                                    marker.remove()
                                }
                            }
                        }
                    }
                }
                delay(5000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}