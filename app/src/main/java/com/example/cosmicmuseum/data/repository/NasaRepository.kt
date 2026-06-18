package com.example.cosmicmuseum.data.repository

import com.example.cosmicmuseum.data.remote.NasaDto
import com.example.cosmicmuseum.data.remote.RetrofitInstance

class NasaRepository {

    suspend fun getApod(): NasaDto {
        return RetrofitInstance.api.getApod("DEMO_KEY")
    }
}