package com.ys.commerce.fusedlocation

sealed interface PermissionState {
    data object Granted : PermissionState
    data object Denied : PermissionState
    data object PermanentlyDenied : PermissionState
}