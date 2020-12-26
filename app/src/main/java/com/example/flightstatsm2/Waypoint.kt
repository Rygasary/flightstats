package com.example.flightstatsm2

data class Waypoint(
    val time: String,
    val lat: String,
    val long: String,
    val altitude: String,
    val rotation: String,
    val isOnGround: Boolean
)