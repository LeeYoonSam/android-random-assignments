package com.ys.commerce

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ys.commerce.fusedlocation.FusedLocationScreen
import com.ys.commerce.ui.theme.CommerceAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CommerceAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FusedLocationScreen(
                        modifier = Modifier.padding(innerPadding)
                    )

//                    /**
//                     * 리스트/상세 네비게이션 컴포즈
//                     */
//                    NavigationHome(
//                        modifier = Modifier.padding(innerPadding)
//                    )

                    /**
                     * 폼 유효성 검사 화면
                     */
//                    FormValidationScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )

                    /**
                     * 상품 상세 화면
                     */
//                    ProductDetailScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )

                    /**
                     * 상품 리스트 화면
                     */
//                    ProductScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }

    fun requestPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                }
                else -> {
                    // No location access granted.
                }
            }
        }

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions:
        // https://developer.android.com/training/permissions/requesting#request-permission
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

}