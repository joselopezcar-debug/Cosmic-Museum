package com.example.cosmicmuseum.data.remote

import retrofit2.http.*

interface NasaApiService {

    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key") apiKey: String
    ): NasaDto
}