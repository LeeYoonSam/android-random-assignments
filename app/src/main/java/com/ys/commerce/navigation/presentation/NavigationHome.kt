package com.ys.commerce.navigation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavigationHome(modifier: Modifier) {
    val navController = rememberNavController()

    Box(modifier = modifier.fillMaxSize()) {
        AppNavHost(navController)
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destinations.ListScreen) {
        composable(Destinations.ListScreen) {
            NavigationListScreen { clickedItem ->
                navController.navigate(Destinations.detailScreen(item = clickedItem))
            }
        }

        composable(
            route = Destinations.DetailScreenRoute,
            arguments = listOf(navArgument(Destinations.DetailArg) { type = NavType.StringType })
        ) { backStackEntry ->
            // NavBackStackEntry에서 arguments를 통해 itemId 가져오기
            val item = backStackEntry.arguments?.getString(Destinations.DetailArg)
            if (item != null) {
                // DetailScreen Composable 호출 (ViewModel이 itemId를 사용)
                NavigationDetailScreen(fruit = item)
            } else {
                // ID가 없는 경우 에러 처리
                Text("Error: Item ID not found.")
            }
        }
    }
}

object Destinations {
    const val ListScreen = "list"
    private const val DetailScreenBase = "detail" // 상세 화면 기본 경로
    const val DetailArg = "item"
    const val DetailScreenRoute = "$DetailScreenBase/{$DetailArg}" // 전체 라우트 패턴

    // 상세 화면으로 이동하기 위한 라우트 문자열 생성 함수
    fun detailScreen(item: String) = "$DetailScreenBase/$item"

}