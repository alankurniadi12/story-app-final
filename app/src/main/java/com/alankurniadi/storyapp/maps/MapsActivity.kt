package com.alankurniadi.storyapp.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.databinding.ActivityMapsBinding
import com.alankurniadi.storyapp.databinding.CustomeInfoWindowsBinding
import com.alankurniadi.storyapp.model.ListStoryItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val args: MapsActivityArgs by navArgs()
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

        dataMarker = args.dataMaps.toList()
        Log.e("MapsActivity", "onCreate:$dataMarker ")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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

        getMyLocation()
        userMarkerStory()
    }

    private fun userMarkerStory() {
        dataMarker.forEach { data ->
            Log.e("MapsActivity", "listStoryItem: ${data.name} ${data.lat}, ${data.lon}")
            if (data.lat != null && data.lon != null) {
                val latLng = LatLng(data.lat, data.lon)
                val addressName = getAddressName(data.lat, data.lon)
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(data.name).snippet(addressName)
                )
                boundsBuilder.include(latLng)
            }

            mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
                override fun getInfoContents(marker: Marker): View? {
                    return null
                }
                override fun getInfoWindow(marker: Marker): View {
                    val bindingIw: CustomeInfoWindowsBinding =
                        CustomeInfoWindowsBinding.inflate(
                            LayoutInflater.from(applicationContext),
                            null, false
                        )
                    Glide.with(this@MapsActivity)
                        .asBitmap()
                        .load(data.photoUrl)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                bindingIw.ivPhoto.setImageBitmap(resource)
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                    bindingIw.tvUserName.text = marker.title
                    return bindingIw.root
                }
            })
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
}
