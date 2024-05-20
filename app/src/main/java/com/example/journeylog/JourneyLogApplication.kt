package com.example.journeylog

import android.app.Application
import com.example.journeylog.data.PhotoSaverRepository

class JourneyLogApplication : Application() {
    lateinit var photoSaver: PhotoSaverRepository
    override fun onCreate() {
        super.onCreate()
        photoSaver = PhotoSaverRepository(this, this.contentResolver)
    }
}