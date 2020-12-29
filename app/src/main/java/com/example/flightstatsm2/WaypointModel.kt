package com.example.flightstatsm2

data class WaypointModel(
    val time: Int,
    val lat: Long,
    val long: Long,
    val altitude: Long,
    val rotation: Long,
    val isOnGround: Boolean
)