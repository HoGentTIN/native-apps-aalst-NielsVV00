package com.example.project3pt.services

import com.example.project3pt.models.AuthResponse
import okhttp3.RequestBody
import retrofit2.http.*


interface UserService {

    /** /ACCOUNT
     * Request an access token by posting login info
     */
    @Headers("Content-Type: application/json")
    @POST("gebruiker/login")
    suspend fun login(
        @Body request: RequestBody

    ): AuthResponse


    /**
     * Register a user account by posting login details
     */
    @Headers("Content-Type: application/json")
    @POST("gebruiker/register")
    suspend fun register(
        @Body request : RequestBody
    ): AuthResponse
}

