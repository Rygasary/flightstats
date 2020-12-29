package com.example.flightstatsm2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class FlightDetailMapFragment : SupportMapFragment(), OnMapReadyCallback, RequestsManager.RequestListener {
    val trackListLiveData: MutableLiveData<List<TrackModel>> = MutableLiveData()
    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val selectedTrackNameLiveData: MutableLiveData<String> = MutableLiveData()

    fun getSelectedTrackNameLiveData(): LiveData<String> {
        return selectedTrackNameLiveData
    }


    fun search(icao: String, time: Long) {
        val searchTrackDataModel = SearchTrackDataModel(
            icao,
            time
        )
        SearchTracksAsyncTask(this).execute(searchTrackDataModel)
    }

    override fun onRequestSuccess(result: String?) {
        isLoadingLiveData.value = false
    }

    override fun onRequestFailed() {
        isLoadingLiveData.value = false
        Log.e("Request", "problem")
    }

    fun updateSelectedFlightName(trackName: String) {
        selectedTrackNameLiveData.value = trackName
    }

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