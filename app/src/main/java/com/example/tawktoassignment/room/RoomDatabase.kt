package com.example.tawktoassignment.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tawktoassignment.User
import com.example.tawktoassignment.interfaces.UserDao

@Database(entities = [User::class], version = 1)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}