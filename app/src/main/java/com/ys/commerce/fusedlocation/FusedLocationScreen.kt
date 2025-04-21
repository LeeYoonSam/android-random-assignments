package com.ys.commerce.fusedlocation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices

@Composable
fun FusedLocationScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FusedLocationContent()
    }
}

@Composable
fun FusedLocationContent(
    viewModel: FusedLocationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val permissionState = uiState.permissionState
    val locationState = uiState.locationState

    val context = LocalContext.current

    // Check permission function
    fun checkPermissionState() {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            viewModel.updatePermissionState(PermissionState.Granted)
        } else {
            // We need to check if we should show rationale
            val activity = context as? androidx.activity.ComponentActivity
            val shouldShowRationale = activity?.shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ?: false

            if (!shouldShowRationale) {
                viewModel.updatePermissionState(PermissionState.PermanentlyDenied)
            } else {
                viewModel.updatePermissionState(PermissionState.Denied)
            }
        }
    }

    // 초기 권한 확인
    LaunchedEffect(Unit) {
        checkPermissionState()
    }
    
    // 앱이 포그라운드로 돌아올 때 권한 재확인
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // 앱이 다시 활성화될 때마다 권한 상태 확인
                checkPermissionState()
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.updatePermissionState(PermissionState.Granted)
        } else {
            val shouldShowRationale =
                (context as? androidx.activity.ComponentActivity)?.shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ?: false
            viewModel.onPermissionResult(isGranted, shouldShowRationale)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PermissionContent(
            permissionState = permissionState,
            requestPermission = {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        ButtonContent(permissionState = permissionState, viewModel = viewModel)
        Spacer(modifier = Modifier.height(10.dp))
        LocationContent(locationState = locationState)
    }
}

@Composable
fun PermissionContent(permissionState: PermissionState, requestPermission: () -> Unit) {
    val context = LocalContext.current
    
    // 권한 거부 관련 텍스트 색상 정의
    val deniedTextColor = Color(0xFFE57373) // 연한 빨간색
    
    when (permissionState) {
        is PermissionState.Granted -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("위치 권한이 허용 되었습니다.")
            }
        }
        
        is PermissionState.Denied -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "정확한 위치 정보를 표시하려면 위치 권한이 필요합니다.",
                    color = deniedTextColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { requestPermission() }) {
                    Text("권한 요청")
                }
            }
        }
        
        is PermissionState.PermanentlyDenied -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "위치 권한이 영구적으로 거부되었습니다. 앱 기능 사용을 원하시면 앱 설정에서 직접 권한을 허용해주세요.",
                    color = deniedTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }) {
                    Text("앱 설정으로 이동")
                }
            }
        }
    }
}

@Composable
fun LocationContent(locationState: LocationState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when (locationState) {
            is LocationState.Location -> {
                Text(
                    locationState.location,
                    textAlign = TextAlign.Center
                )
            }
            is LocationState.LocationNotFound -> {
                Text(
                    "위도: -, 경도: -",
                    textAlign = TextAlign.Center
                )
            }
            is LocationState.LocationGetFailed -> {
                Text(
                    "위치 정보 가져오기 실패: ${locationState.errorMessage}",
                    color = Color(0xFFE57373),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ButtonContent(
    permissionState: PermissionState,
    viewModel: FusedLocationViewModel
) {
    val context = LocalContext.current

    when (permissionState) {
        is PermissionState.Granted -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = {
                    // API 호출 직전에 권한을 다시 확인하는 것이 안전합니다.
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val fusedLocationClient =
                            LocationServices.getFusedLocationProviderClient(context)

                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location ->
                                viewModel.updateLocation(location)
                            }
                            .addOnFailureListener { exception ->
                                viewModel.updateLocationError("위치 정보를 가져오는 데 실패했습니다: ${exception.message}")
                            }
                    } else {
                        viewModel.updateLocationError("오류: 위치 권한이 없습니다.")
                        Log.e("ButtonContent", "Permission check failed inside onClick")
                    }
                }) {
                    Text("현재 위치 가져오기")
                }
            }
        }
        else -> { /* 아무것도 표시하지 않음 */ }
    }
}