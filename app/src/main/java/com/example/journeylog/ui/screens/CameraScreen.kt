package com.example.journeylog.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun CameraScreen(
    navController: NavHostController,
    viewModel: CameraViewModel = viewModel(factory = CameraViewModel.Factory)
) {

}