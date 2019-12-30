package com.example.project3pt.services

import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Foto
import com.example.project3pt.models.Wedstrijd
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*

interface FotoService {
    @GET("foto/GetRandom")
    suspend fun getRandom(): Foto

    @GET("foto")
    suspend fun getAll(): List<Foto>

    @POST("foto?")
    suspend fun post(@Query("data") data:String): Foto
}