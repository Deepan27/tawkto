package com.example.tawktoassignment.interfaces

import com.example.tawktoassignment.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {

    @GET("users")
    fun getUsers(): Call<List<User>>
    
}