package com.example.project3pt.services

import com.example.project3pt.models.Foto
import okhttp3.RequestBody
import retrofit2.http.*

interface FotoService {
    @GET("foto/GetRandom")
    suspend fun getRandom(): Foto

    @GET("foto")
    suspend fun getAll(): List<Foto>

    @POST("foto")
    suspend fun post(@Body foto: Foto): Foto
}
