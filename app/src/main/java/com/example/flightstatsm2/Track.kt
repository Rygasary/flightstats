package com.example.flightstatsm2

data class Track(
    val icao24: String,
    val callsign: String,
    val startTime: String,
    val endTime: String,
    val path: Array<Waypoint>
)