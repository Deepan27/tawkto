package com.example.tawktoassignment.room

import android.content.Context
import androidx.room.Room
import androidx.room.Room.databaseBuilder

object RoomDatabaseBuilder {
    private var roomDatabase: RoomDatabase? = null

    fun buildDatabase(context: Context): RoomDatabase {
        if (roomDatabase == null) {
            roomDatabase = Room.databaseBuilder(
                context.applicationContext,
                RoomDatabase::class.java,
                "my-database"
            ).build()
        }
        return roomDatabase!!
    }
}