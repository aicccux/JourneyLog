package com.example.journeylog.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LogEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
    companion object {
        const val DB_NAME = "log_database"
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//        fun getDatabase(context: Context): AppDatabase {
//            // if the INSTANCE is not null, then return it,
//            // if it is, then create the database
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    DB_NAME
//                ).build()
//                INSTANCE = instance
//                // return instance
//                instance
//            }
//        }
    }
}