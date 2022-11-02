package com.alankurniadi.storyapp.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.navigation.navArgs
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.dataStore
import com.alankurniadi.storyapp.databinding.ActivityMapsBinding
import com.alankurniadi.storyapp.home.ListStoryViewModel
import com.alankurniadi.storyapp.model.ListStoryItem
import com.alankurniadi.storyapp.utils.SettingPreferences
import com.alankurniadi.storyapp.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var dataMarker: List<ListStoryItem>
    private val boundsBuilder = LatLngBounds.Builder()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGrandted: Boolean ->
        if (isGrandted) {
            getMyLocation()
        }
    }

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

        with(mMap.uiSettings) {
            isZoomControlsEnabled = true
            isCompassEnabled = true
        }
        mMap.setOnInfoWindowClickListener(this)

        val pref = SettingPreferences.getInstance(this.dataStore)
        val listStoryVm =
            ViewModelProvider(this, ViewModelFactory(this, pref))[ListStoryViewModel::class.java]
        val mapsViewModel =
            ViewModelProvider(this, ViewModelFactory(this, pref))[MapsStoryViewModel::class.java]

        listStoryVm.getToken().observe(this) {token ->
            if (token != null) {
                mapsViewModel.getAllStoryLocation(token)
            }
        }

        mapsViewModel.dataMaps.observe(this) {
            dataMarker = it.listStory
            if (dataMarker.isNotEmpty()) {
                userMarkerStory()
                setCustomInfoWindow()
            }
        }

        getMyLocation()
    }

    private fun setCustomInfoWindow() {
        val customView: View = layoutInflater.inflate(R.layout.custome_info_windows, null)

        mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoContents(maker: Marker): View {
                render(maker, customView)
                return customView
            }

            override fun getInfoWindow(maker: Marker): View {
                render(maker, customView)
                return customView
            }

        })
    }

    private fun render(marker: Marker, view: View) {
        val name = view.findViewById<TextView>(R.id.tv_user_name)
        val photo = view.findViewById<ImageView>(R.id.iv_photo)

        for (data in dataMarker) {
            Glide.with(applicationContext)
                .load(data.photoUrl)
                .into(photo)
            name.text = marker.title
        }
    }

    private fun userMarkerStory() {
        dataMarker.forEach { data ->
            if (data.lat != null && data.lon != null) {
                val latLng = LatLng(data.lat, data.lon)
                val addressName = getAddressName(data.lat, data.lon)
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(data.name).snippet(addressName)
                )
                boundsBuilder.include(latLng)
            }
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    override fun onInfoWindowClick(marker: Marker) {
        marker.showInfoWindow()
    }
}
