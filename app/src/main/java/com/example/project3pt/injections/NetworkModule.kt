package com.example.project3pt.injections

import android.content.Context
import com.example.project3pt.services.UserService
import com.example.project3pt.services.WedstrijdService
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.google.gson.GsonBuilder


@Module
class NetworkModule(private val context: Context) {

    @Provides
    internal fun provideUserService(retrofit: Retrofit) : UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    internal fun provideWedstrijdService(retrofit: Retrofit) : WedstrijdService {
        return retrofit.create(WedstrijdService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(ServiceInterceptor(context))
            .build()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl("https://wedstrijdapi.azurewebsites.net/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}