package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.MediaItem

@Database(entities = [MediaItem::class], version = 1, exportSchema = false)
abstract class DoomsDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao

    companion object {
        @Volatile
        private var INSTANCE: DoomsDatabase? = null

        fun getDatabase(context: Context): DoomsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DoomsDatabase::class.java,
                    "dooms_marvel.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
