package com.example.tawktoassignment

import com.example.tawktoassignment.interfaces.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApiClient {

    const val BASE_URL = "https://api.github.com/"

    val userApiService : UserApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UserApiService::class.java)
    }
}