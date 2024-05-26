package com.example.journeylog.ui.screens

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.journeylog.database.MAX_LOG_PHOTOS_LIMIT
import com.example.journeylog.ui.Screens
import com.example.journeylog.ui.components.CameraExplanationDialog
import com.example.journeylog.ui.components.DatePickerCompose
import com.example.journeylog.ui.components.LocationExplanationDialog
import com.example.journeylog.ui.components.LocationPicker
import com.example.journeylog.ui.components.PhotoGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLogScreen(
    navController: NavHostController,
    viewModel: AddLogViewModel = viewModel(factory = AddLogViewModel.Factory)
) {
    // region State initialization
    val state = viewModel.uiState
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    fun canAddPhoto(callback: () -> Unit) {
        if (viewModel.canAddPhoto()) {
            callback()
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("You can't add more than $MAX_LOG_PHOTOS_LIMIT photos")
            }
        }
    }
    // endregion

    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.onPermissionChange(Manifest.permission.CAMERA, isGranted)
                canAddPhoto {
                    navController.navigate(Screens.Camera.route)
                }
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Camera currently disabled due to denied permission.")
                }
            }
        }

    var showExplanationDialogForCameraPermission by remember { mutableStateOf(false) }
    if (showExplanationDialogForCameraPermission) {
        CameraExplanationDialog(
            onConfirm = {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
                showExplanationDialogForCameraPermission = false
            },
            onDismiss = { showExplanationDialogForCameraPermission = false },
        )
    }

    val requestLocationPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.onPermissionChange(Manifest.permission.ACCESS_COARSE_LOCATION, isGranted)
                viewModel.fetchLocation()
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Location currently disabled due to denied permission.")
                }
            }
        }

    var showExplanationDialogForLocationPermission by remember { mutableStateOf(false) }
    if (showExplanationDialogForLocationPermission) {
        LocationExplanationDialog(
            onConfirm = {
                requestLocationPermissions.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                showExplanationDialogForLocationPermission = false
            },
            onDismiss = { showExplanationDialogForLocationPermission = false }
        )
    }
    // region helper functions

    LaunchedEffect(Unit) {
        viewModel.refreshSavedPhotos()
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.navigate(Screens.Home.route) {
                popUpTo(Screens.Home.route) {
                    inclusive = false
                }
            }
        }
    }

    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(MAX_LOG_PHOTOS_LIMIT),
        viewModel::onPhotoPickerSelect
    )

    fun canSaveLog(callback: () -> Unit) {
        if (viewModel.isValid()) {
            callback()
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("You haven't completed all details")
            }
        }
    }
    // endregion
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Add Log", fontFamily = FontFamily.Serif) },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Save log") },
                icon = {
                    if (state.isSaving) {
                        CircularProgressIndicator(Modifier.size(24.0.dp))
                    } else {
                        Icon(Icons.Filled.Check, null)
                    }
                },
                onClick = {
                    canSaveLog {
                        viewModel.createLog()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            // region Date
            ListItem(
                headlineContent = { Text("Date") },
                trailingContent = {
                    DatePickerCompose(
                        state.date,
                        onChange = viewModel::onDateChange
                    )
                }
            )
            HorizontalDivider()
            // endregion

            // region Location
            ListItem(
                headlineContent = { Text("Location") },
                trailingContent = {
                    LocationPicker(state.place) {
                        when {
                            state.hasLocationAccess -> viewModel.fetchLocation()
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                context.applicationContext as Activity,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) ->
                                showExplanationDialogForLocationPermission = true

                            else -> requestLocationPermissions.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                        }
                    }
                }
            )
            HorizontalDivider()
            // endregion

            // region Photos
            ListItem(
                headlineContent = { Text("Photos") },
                trailingContent = {
                    Row {
                        // region Photo Picker
                        TextButton(onClick = {
                            canAddPhoto {
                                viewModel.loadLocalPickerPictures()
                                coroutineScope.launch {
                                    pickImage.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                            }
                        }) {
                            Icon(Icons.Filled.ShoppingCart, null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Add photo")
                        }
                        // endregion

                        // region Camera
                        IconButton(onClick = {
                            canAddPhoto {
                                when {
                                    state.hasCameraAccess -> navController.navigate(Screens.Camera.route)
                                    ActivityCompat.shouldShowRequestPermissionRationale(
                                        context.applicationContext as Activity,
                                        Manifest.permission.CAMERA
                                    ) -> showExplanationDialogForCameraPermission = true

                                    else -> requestCameraPermission.launch(Manifest.permission.CAMERA)
                                }
                            }
                        })
                        {
                            Icon(Icons.Filled.Add, null)
                        }
                        // endregion
                    }
                }
            )
            // endregion

            PhotoGrid(
                modifier = Modifier.padding(16.dp),
                photos = state.savedPhotos,
                onRemove = { photo -> viewModel.onPhotoRemoved(photo) }
            )
        }
    }
}