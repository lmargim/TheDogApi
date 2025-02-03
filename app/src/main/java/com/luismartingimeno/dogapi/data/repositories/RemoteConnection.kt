package com.luismartingimeno.dogapi.data.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteConnection {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.thedogapi.com/v1/") // Base URL de The Dog API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val remoteService: RemoteService = retrofit.create(RemoteService::class.java)
}
