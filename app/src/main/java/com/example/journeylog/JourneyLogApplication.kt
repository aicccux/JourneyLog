package com.example.journeylog

import android.app.Application
import androidx.room.Room
import com.example.journeylog.data.PhotoSaverRepository
import com.example.journeylog.database.AppDatabase

class JourneyLogApplication : Application() {
    lateinit var photoSaver: PhotoSaverRepository

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        photoSaver = PhotoSaverRepository(this, this.contentResolver)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, AppDatabase.DB_NAME)
            .build()
    }
}