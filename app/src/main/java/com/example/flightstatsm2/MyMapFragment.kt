package com.example.flightstatsm2

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MyMapFragment : SupportMapFragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    override fun onMapReady(gmap: GoogleMap) {
        googleMap = gmap

        // Set default position
        // Add a marker in Sydney and move the camera
        val vietnam = LatLng(14.0583, 108.2772) // 14.0583° N, 108.2772° E
        googleMap!!.addMarker(MarkerOptions().position(vietnam).title("Marker in Vietnam"))
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(vietnam))
        googleMap!!.setOnMapClickListener { latLng ->
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)
            // Clear previously click position.
            googleMap!!.clear()
            // Zoom the Marker
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            // Add Marker on Map
            googleMap!!.addMarker(markerOptions)
        }
    }

    init {
        getMapAsync(this)
    }
}