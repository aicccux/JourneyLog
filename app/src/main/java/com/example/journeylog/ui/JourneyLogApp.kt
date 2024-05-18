package com.example.journeylog.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.journeylog.ui.screens.AddLogScreen
import com.example.journeylog.ui.screens.CameraScreen
import com.example.journeylog.ui.screens.HomeScreen

@Composable
fun JourneyLogApp() {
    val navController = rememberNavController()
    val startNavigation = Screens.Home.route
    NavHost(navController = navController, startDestination = startNavigation) {
        composable(Screens.Home.route) { HomeScreen(navController) }
        composable(Screens.AddLog.route) { AddLogScreen(navController) }
        composable(Screens.Camera.route) { CameraScreen(navController) }
    }
}

sealed class Screens(val route: String) {
    data object Home : Screens("HomeScreen")
    data object AddLog : Screens("AddLogScreen")
    data object Camera : Screens("CameraScreen")
}