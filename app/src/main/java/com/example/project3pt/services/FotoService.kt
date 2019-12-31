package com.example.project3pt.services

import com.example.project3pt.models.Foto
import retrofit2.http.*

interface FotoService {
    @GET("foto/GetRandom")
    suspend fun getRandom(): Foto

    @GET("foto")
    suspend fun getAll(): List<Foto>

    @POST("foto?")
    suspend fun post(@Query("data") data: String): Foto
}
