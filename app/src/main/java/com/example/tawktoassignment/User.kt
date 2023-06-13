package com.example.tawktoassignment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(@PrimaryKey val login : String, val id : Int, val avatar_url: String,
                val company : String?, val blog: String?, val name : String?, val followers : String?, val following : String? )
