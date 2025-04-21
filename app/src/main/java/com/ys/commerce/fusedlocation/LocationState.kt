package com.ys.commerce.fusedlocation

sealed interface LocationState {
    data class Location(val location: String) : LocationState
    data object LocationNotFound: LocationState
    data class LocationGetFailed(val errorMessage: String): LocationState
}