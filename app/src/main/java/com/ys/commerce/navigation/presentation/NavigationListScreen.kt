package com.ys.commerce.navigation.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationListScreen(
    onItemClick: (fruit: String) -> Unit
) {

    val fruits = remember { listOf("Apple", "Banana", "Cherry", "Date", "Elderberry") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "과일 목록",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent,
            ),
        )
        NavigationList(
            fruits = fruits,
            onClickDetailScreen = onItemClick
        )
    }
}

@Composable
fun NavigationList(
    fruits: List<String>,
    onClickDetailScreen: (fruit: String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = fruits, key = { it }) { fruit ->
            FruitCard(fruit, onClickDetailScreen)
        }
    }
}

@Composable
fun FruitCard(
    fruit: String,
    onClickDetailScreen: (fruit: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClickDetailScreen(fruit) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = fruit,
            fontSize = 24.sp
        )
    }
}