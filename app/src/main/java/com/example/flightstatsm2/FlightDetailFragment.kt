package com.example.flightstatsm2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_flight_detail.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FlightDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlightDetailFragment : Fragment(), OnMapReadyCallback, RequestsManager.RequestListener, GoogleMap.OnMapLoadedCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var searchIcao: String? = null
    private var searchTime: Long? = null

    private lateinit var viewModel: FlightListViewModel
    private lateinit var mMapView: MapView
    private lateinit var myGoogleMap: GoogleMap

    private lateinit var departureArrival: DepartureArrival


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity()).get(FlightListViewModel::class.java)
        viewModel.getSelectedFlightNameLiveData().observe(this, androidx.lifecycle.Observer{
            flight_name.text = it
        })


        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_flight_detail, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(FlightListViewModel::class.java)
        searchIcao = viewModel.getSelectedIcao()
        searchTime = viewModel.getSelectedTime()
        searchTrack(searchIcao!!, searchTime!!)

        mMapView = rootView.findViewById(R.id.mapView) as MapView
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()
        return rootView
    }

    override fun onRequestSuccess(result: String?) {
        val trackModel: TrackModel = Utils.getTrackFromString(result!!)
        departureArrival = DepartureArrival(LatLng(trackModel.path[0].lat.toDouble(),trackModel.path[0].long.toDouble()),
                                            LatLng(trackModel.path.last().lat.toDouble(),trackModel.path.last().long.toDouble()))
    }

    override fun onRequestFailed() {
        Log.e("Request", "problem")
    }

    fun searchTrack(icao: String, time:Long) {

        val searchTrackDataModel = SearchTrackDataModel(icao, time)
        SearchTracksAsyncTask(this).execute(searchTrackDataModel)
    }
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FlightDetailFragment().apply {

            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myGoogleMap = googleMap
        myGoogleMap.setOnMapLoadedCallback(this)
        myGoogleMap.addMarker(
            MarkerOptions().position(departureArrival.departureCoordinates)
        )
        myGoogleMap.addMarker(
            MarkerOptions().position(departureArrival.arrivalCoordinates)
        )

    }

    override fun onMapLoaded() {
        this.zoomToFit(departureArrival.departureCoordinates, departureArrival.arrivalCoordinates)
    }
    private fun zoomToFit(departure: LatLng?, arrival: LatLng?) {
        val coordinates = LatLngBounds.Builder()
            .include(departure)
            .include(arrival)
            .build()

        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(coordinates, 400))
    }
}