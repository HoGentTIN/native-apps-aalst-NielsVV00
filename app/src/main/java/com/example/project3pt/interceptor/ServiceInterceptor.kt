package com.example.project3pt.interceptor

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Klasse voor het toevoegen van de authentication aan elke request, vergelijkbaar met de AuthInterceptor in Angular
 * Dit zorgt ervoor dat je data kan ophalen uit de API zonder steeds een 401 error te krijgen
 */
class ServiceInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if( request.header("Authentication") == null){
            val token = context.getSharedPreferences("Preferences",0).getString("userToken", null)
            Log.i("token intercept", token.toString())
            if(!token.isNullOrEmpty()){
                val actualToken = "Bearer " + token
                request = request
                    .newBuilder()
                    .addHeader("Authorization",actualToken)
                    .build()
            }
        }
        return chain.proceed(request)
    }
}
