package com.angelina.lvivexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.angelina.lvivexplorer.feature.detail.PlaceDetailScreen
import com.angelina.lvivexplorer.feature.detail.PlaceDetailViewModel
import com.angelina.lvivexplorer.feature.diary.DiaryScreen
import com.angelina.lvivexplorer.feature.diary.DiaryViewModel
import com.angelina.lvivexplorer.feature.filter.FilterScreen
import com.angelina.lvivexplorer.feature.map.MapScreen
import com.angelina.lvivexplorer.feature.map.MapViewModel
import com.angelina.lvivexplorer.feature.settings.SettingsScreen
import com.angelina.lvivexplorer.feature.settings.SettingsViewModel

@Composable
fun LvivExplorerApp() {
    val navController = rememberNavController()
    val mapViewModel: MapViewModel = hiltViewModel()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val topLevelItems = listOf(
        BottomNavItem("Map", Destinations.Map.baseRoute, Icons.Default.Map),
        BottomNavItem("Diary", Destinations.Diary.route, Icons.Default.List),
        BottomNavItem("Settings", Destinations.Settings.route, Icons.Default.Settings)
    )

    val showBottomBar = currentRoute in setOf(Destinations.Map.route, Destinations.Diary.route, Destinations.Settings.route)

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topLevelItems.forEach { item ->
                        NavigationBarItem(
                            selected = if (item.route == Destinations.Map.baseRoute) {
                                currentRoute == Destinations.Map.route
                            } else {
                                currentRoute == item.route
                            },
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.Map.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Destinations.Map.route,
                arguments = listOf(
                    navArgument("focusPlaceId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val focusPlaceId = backStackEntry.arguments?.getString("focusPlaceId")
                MapScreen(
                    viewModel = mapViewModel,
                    focusPlaceId = focusPlaceId,
                    onOpenFilter = { navController.navigate(Destinations.Filter.route) },
                    onOpenDetails = { placeId ->
                        navController.navigate(Destinations.Detail.routeWithArg(placeId))
                    }
                )
            }
            composable(Destinations.Filter.route) {
                FilterScreen(
                    viewModel = mapViewModel,
                    onApplyAndBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Destinations.Detail.route,
                arguments = listOf(navArgument("placeId") { type = NavType.StringType })
            ) {
                val detailViewModel: PlaceDetailViewModel = hiltViewModel()
                PlaceDetailScreen(
                    viewModel = detailViewModel,
                    onDone = { navController.popBackStack() },
                    onShowOnMap = { placeId ->
                        navController.navigate(Destinations.Map.routeWithFocus(placeId)) {
                            popUpTo(Destinations.Map.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Destinations.Diary.route) {
                val diaryViewModel: DiaryViewModel = hiltViewModel()
                DiaryScreen(
                    viewModel = diaryViewModel,
                    onOpenDetails = { placeId ->
                        navController.navigate(Destinations.Detail.routeWithArg(placeId))
                    }
                )
            }
            composable(Destinations.Settings.route) {
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}

private data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
