package com.example.flightstatsm2

import com.google.android.gms.maps.model.LatLng

data class DepartureArrival (
    val departureCoordinates: LatLng,
    val arrivalCoordinates: LatLng
)
