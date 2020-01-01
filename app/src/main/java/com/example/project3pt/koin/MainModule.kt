package com.example.project3pt.koin

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.example.project3pt.database.Database3PT
import com.example.project3pt.database.WedstrijdDao
import com.example.project3pt.fragments.foto.FotoViewModel
import com.example.project3pt.fragments.home.HomeViewModel
import com.example.project3pt.activities.login.LoginViewModel
import com.example.project3pt.fragments.maakWedstrijd.MaakWedstrijdViewModel
import com.example.project3pt.activities.register.RegistreerViewModel
import com.example.project3pt.fragments.wedstrijd.WedstrijdViewModel
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdLijstViewModel
import com.example.project3pt.interceptor.ServiceInterceptor
import com.example.project3pt.repositories.FotoRepository
import com.example.project3pt.repositories.UserRepository
import com.example.project3pt.repositories.WedstrijdRepository
import com.example.project3pt.services.*
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {

    viewModel {
        RegistreerViewModel(
            get()
        )
    }
    viewModel { LoginViewModel(get()) }
    viewModel { WedstrijdViewModel(get()) }
    viewModel { WedstrijdLijstViewModel(get()) }
    viewModel { FotoViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MaakWedstrijdViewModel(get()) }

    single { WedstrijdRepository(get(), get(), get(), get(), get()) }
    single { FotoRepository(get()) }
    single { UserRepository(get(), get()) }

    single { provideWedstrijdDao(get()) }

    single { Database3PT.getInstance(androidContext()) }

    single { provideConnectivityManager(androidContext()) }
    single { provideSharedPreferences(androidContext()) }
    single { provideFotoService(get()) }
    single { provideUserService(get()) }
    single { provideWedstrijdService(get()) }
    single { provideRetrofit(get(), androidContext()) }
    single { provideMoshi() }
}

fun provideWedstrijdDao(database: Database3PT): WedstrijdDao {
    return database.wedstrijdDao
}

fun provideConnectivityManager(context: Context): ConnectivityManager {
    return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}

fun provideFotoService(retrofit: Retrofit): FotoService {
    return retrofit.create(FotoService::class.java)
}

fun provideUserService(retrofit: Retrofit): UserService {
    return retrofit.create(UserService::class.java)
}

fun provideWedstrijdService(retrofit: Retrofit): WedstrijdService {
    return retrofit.create(WedstrijdService::class.java)
}

fun provideSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("Preferences", 0)
}

fun provideRetrofit(moshi: Moshi, context: Context): Retrofit {
    val client = OkHttpClient.Builder()
        .addInterceptor(ServiceInterceptor(context))
        .build()

    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .setLenient()
        .create()

    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl("https://wedstrijdapibackup.azurewebsites.net/api/")  //.baseUrl("https://wedstrijdapi.azurewebsites.net/api/")
        .build()
}

fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build() as Moshi
}
