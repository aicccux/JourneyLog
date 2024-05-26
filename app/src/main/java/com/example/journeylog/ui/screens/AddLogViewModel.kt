package com.example.journeylog.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.journeylog.JourneyLogApplication
import com.example.journeylog.data.MediaRepository
import com.example.journeylog.data.PhotoSaverRepository
import com.example.journeylog.database.LogEntry
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddLogViewModel(application: Application, private val photoSaver: PhotoSaverRepository) :
    ViewModel() {
    private val appContext: Context = application.applicationContext
    private val mediaRepository = MediaRepository(appContext)
    private val dbDao = JourneyLogApplication.db.logDao()
    data class UiState(
        val hasLocationAccess: Boolean,
        val hasCameraAccess: Boolean,
        val isSaving: Boolean = false,
        val isSaved: Boolean = false,
        val date: Long,
        val place: String? = null,
        val savedPhotos: List<File> = emptyList(),
        val localPickerPhotos: List<Uri> = emptyList()
    )

    var uiState by mutableStateOf(
        UiState(
            hasLocationAccess = hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION),
            hasCameraAccess = hasPermission(Manifest.permission.CAMERA),
            date = getTodayDateInMillis(),
            savedPhotos = photoSaver.getPhotos()
        )
    )

    fun isValid(): Boolean {
        return uiState.place != null && !photoSaver.isEmpty() && !uiState.isSaving
    }

    private fun getTodayDateInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    private fun getIsoDate(timeInMillis: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(timeInMillis)
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onPermissionChange(permission: String, isGranted: Boolean) {
        when (permission) {
            Manifest.permission.ACCESS_COARSE_LOCATION -> {
                uiState = uiState.copy(hasLocationAccess = isGranted)
            }

            Manifest.permission.CAMERA -> {
                uiState = uiState.copy(hasCameraAccess = isGranted)
            }

            else -> {
                android.util.Log.e("Permission change", "Unexpected permission: $permission")
            }
        }
    }

    fun onDateChange(dateInMillis: Long) {
        uiState = uiState.copy(date = dateInMillis)
    }
    // region Location management
    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener

            val geocoder = Geocoder(appContext, Locale.getDefault())

            if (Build.VERSION.SDK_INT >= 33)
            {
                geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                    val address = addresses.firstOrNull()
                    val place = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                    ?: address?.countryName
                    uiState = uiState.copy(place = place)
                }
            }
            else {
                val address =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
                        ?: return@addOnSuccessListener
                val place =
                    address.locality ?: address.subAdminArea ?: address.adminArea ?: address.countryName
                    ?: return@addOnSuccessListener

                uiState = uiState.copy(place = place)
            }
        }
    }
    // endregion
    fun loadLocalPickerPictures() {
        viewModelScope.launch {
            val localPickerPhotos = mediaRepository.fetchImages().map { it.uri }.toList()
            uiState = uiState.copy(localPickerPhotos = localPickerPhotos)
        }
    }

    fun onLocalPhotoPickerSelect(photo: Uri) {
        viewModelScope.launch {
            photoSaver.cacheFromUri(photo)
            refreshSavedPhotos()
        }
    }

    fun onPhotoPickerSelect(photos: List<Uri>) {
        viewModelScope.launch {
            photoSaver.cacheFromUris(photos)
            refreshSavedPhotos()
        }
    }

    fun canAddPhoto() = photoSaver.canAddPhoto()

    fun refreshSavedPhotos() {
        uiState = uiState.copy(savedPhotos = photoSaver.getPhotos())
    }

    fun onPhotoRemoved(photo: File) {
        viewModelScope.launch {
            photoSaver.removeFile(photo)
            refreshSavedPhotos()
        }
    }

    fun createLog() {
        if (!isValid()) {
            return
        }

        uiState = uiState.copy(isSaving = true)

        viewModelScope.launch {
            val photos = photoSaver.savePhotos()

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = uiState.date
            android.util.Log.e("date is ", uiState.date.toString())

            val log = LogEntry(
                date = getIsoDate(uiState.date),
                place = uiState.place!!,
                photo1 = photos[0].name,
                photo2 = photos.getOrNull(1)?.name,
                photo3 = photos.getOrNull(2)?.name,
            )

            dbDao.insert(log)
            uiState = uiState.copy(isSaved = true)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val application = (this[APPLICATION_KEY] as JourneyLogApplication)
                AddLogViewModel(
                    application, application.photoSaver
                )
            }
        }
    }
}