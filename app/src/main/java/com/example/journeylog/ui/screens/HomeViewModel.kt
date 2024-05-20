package com.example.journeylog.ui.screens


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.example.journeylog.JourneyLogApplication
import com.example.journeylog.database.AppDatabase
import com.example.journeylog.database.AppDatabase.Companion.DB_NAME
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : ViewModel() {

    val db = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java, DB_NAME
    ).build()

    fun loadLog() {
        viewModelScope.launch {

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val application = (this[APPLICATION_KEY] as JourneyLogApplication)
                HomeViewModel(
                    application
                )
            }
        }
    }
}