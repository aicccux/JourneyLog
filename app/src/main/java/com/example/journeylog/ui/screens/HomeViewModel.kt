package com.example.journeylog.ui.screens


import android.app.Application
import android.content.Context
import android.text.format.DateUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.example.journeylog.JourneyLogApplication
import com.example.journeylog.data.PhotoSaverRepository
import com.example.journeylog.database.AppDatabase
import com.example.journeylog.database.AppDatabase.Companion.DB_NAME
import com.example.journeylog.database.Log
import kotlinx.coroutines.launch

class HomeViewModel(application: Application, private val photoSaver: PhotoSaverRepository) : ViewModel() {
    private val appContext: Context = application.applicationContext

    private val db = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java, DB_NAME
    ).build()

    data class UiState(val loading: Boolean = true, val logs: List<Log> = emptyList())

    private var uiState by mutableStateOf(UiState())

    fun formatDateTime(timeInMillis: Long): String {
        return DateUtils.formatDateTime(appContext, timeInMillis, DateUtils.FORMAT_ABBREV_ALL)
    }

    private fun loadLogs() {
        viewModelScope.launch {
            uiState = uiState.copy(
                loading = false,
                logs = db.logDao().getAllWithFiles(photoSaver.photoFolder)
            )
        }
    }

    fun delete(log: Log) {
        viewModelScope.launch {
            db.logDao().delete(log.toLogEntry())
            loadLogs()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val application = (this[APPLICATION_KEY] as JourneyLogApplication)
                HomeViewModel(
                    application, application.photoSaver
                )
            }
        }
    }
}