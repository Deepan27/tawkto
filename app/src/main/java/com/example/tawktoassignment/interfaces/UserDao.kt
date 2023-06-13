package com.example.tawktoassignment.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tawktoassignment.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getUsers() : List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)
}