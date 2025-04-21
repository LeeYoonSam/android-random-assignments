package com.ys.commerce.fusedlocation

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class FusedLocationState(
    val permissionState: PermissionState = PermissionState.Denied,
    val locationState: LocationState = LocationState.LocationNotFound
)

class FusedLocationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FusedLocationState())

    val uiState: StateFlow<FusedLocationState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FusedLocationState()
        )

    fun updatePermissionState(permissionState: PermissionState) {
        _uiState.update { currentState ->
            currentState.copy(permissionState = permissionState)
        }
    }

    fun onPermissionResult(isGranted: Boolean, shouldShowRationale: Boolean) {
        val newPermissionState = if (isGranted) {
            PermissionState.Granted
        } else {
            if (!shouldShowRationale) {
                PermissionState.PermanentlyDenied
            } else {
                PermissionState.Denied
            }
        }

        _uiState.update { currentState ->
            currentState.copy(permissionState = newPermissionState)
        }
    }

    fun updateLocation(location: Location?) {
        val newLocationState = if (location != null) {
            LocationState.Location("위도: ${location.latitude}, 경도: ${location.longitude}")
        } else {
            LocationState.Location("저장된 마지막 위치를 찾을 수 없습니다. (기기 위치 설정 확인)")
                .also { Log.w("FusedLocationViewModel", "Last location returned null.") }
        }

        _uiState.update { currentState ->
            currentState.copy(locationState = newLocationState)
        }
    }

    fun updateLocationError(errorMessage: String) {
        val newLocationState = LocationState.LocationGetFailed(errorMessage)
        Log.e("FusedLocationViewModel", "Error getting last location: $errorMessage")

        _uiState.update { currentState ->
            currentState.copy(locationState = newLocationState)
        }
    }
}