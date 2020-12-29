package com.example.flightstatsm2

data class WaypointModel(
    val time: Int,
    val lat: Double,
    val long: Double,
    val altitude: Long,
    val rotation: Long,
    val isOnGround: Boolean
)