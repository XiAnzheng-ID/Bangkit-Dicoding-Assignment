package com.devin.storykw.ui.drawer.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.devin.storykw.R
import com.devin.storykw.backend.RetrofitClient
import com.devin.storykw.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        fetchStoriesWithLocation()
    }

    private fun fetchStoriesWithLocation() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiStory.getStoriesWithLoc(location = 1)
                if (!response.error!!) {
                    response.listStory?.forEach { storyItem ->
                        storyItem?.let {
                            val position = LatLng(it.lat ?: 0.0, it.lon ?: 0.0)
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(position)
                                    .title(it.name)
                                    .snippet(it.description)
                            )
                        }
                    }

                    response.listStory?.firstOrNull()?.let {
                        val firstPosition = LatLng(it.lat ?: 0.0, it.lon ?: 0.0)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPosition, 10f))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}