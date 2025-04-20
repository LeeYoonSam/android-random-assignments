package com.ys.commerce.navigation.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDetailScreen(
    fruit: String
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "과일 상세",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent,
            ),
        )
        NavigationDetailContent(fruit)
    }
}

@Composable
fun NavigationDetailContent(fruit: String) {
    Text(
        text = fruit,
        fontSize = 24.sp,
        modifier = Modifier.padding(16.dp)
    )
}
