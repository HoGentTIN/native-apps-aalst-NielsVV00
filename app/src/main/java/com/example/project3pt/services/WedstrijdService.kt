package com.example.project3pt.services

import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Wedstrijd
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WedstrijdService {
    @GET("wedstrijd")
    suspend fun getWedstrijden(): List<Wedstrijd>

    @GET("wedstrijd/{id}")
    suspend fun getWedstrijd(@Path("id") id: Long): Wedstrijd

    @POST("wedstrijd")
    suspend fun postWedstrijd(
        @Body request: RequestBody
    ): ResponseBody

    @GET("wedstrijd/{id}/deelnemers")
    suspend fun getDeelnemers(@Path("id") id: Long): List<Deelnemer>

    @POST("wedstrijd/{id}/neemdeel")
    suspend fun neemDeel(@Path("id") id: Long): ResponseBody
}
