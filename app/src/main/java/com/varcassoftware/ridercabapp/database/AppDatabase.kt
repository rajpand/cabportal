package com.varcassoftware.ridercabapp.database
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): AppDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
