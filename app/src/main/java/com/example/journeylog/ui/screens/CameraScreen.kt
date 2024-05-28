package com.example.journeylog.ui.screens

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.journeylog.R

@Composable
fun CameraScreen(
    navController: NavHostController,
    viewModel: CameraViewModel = viewModel(factory = CameraViewModel.Factory)
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.cameraState

    val previewUseCase = remember { Preview.Builder().build() }

    LaunchedEffect(Unit) {
        val cameraProvider = viewModel.getCameraProvider()
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                previewUseCase,
                state.imageCapture
            )
        } catch (ex: Exception) {
            Log.e("CameraCapture", "Failed to bind camera use cases", ex)
        }
    }

    LaunchedEffect(state.imageFile) {
        if (state.imageFile != null) {
            navController.popBackStack()
        }
    }

    Scaffold(
        floatingActionButton = {
            Button(
                onClick = { if (!state.isTakingPicture) viewModel.takePicture() }
            ) {
                Icon(painter = painterResource(id = R.drawable.outline_photo_camera_24), contentDescription = "Take picture")
            }
        }
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                    previewUseCase.setSurfaceProvider(this.surfaceProvider)
                }
            }
        )
    }
}