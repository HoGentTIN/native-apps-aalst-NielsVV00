package com.example.project3pt

import android.app.Application
import com.example.project3pt.injections.AppComponent
import com.example.project3pt.injections.DaggerAppComponent
import com.example.project3pt.injections.DatabaseModule
import com.example.project3pt.injections.NetworkModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


class App: Application() {

    companion object{
        lateinit var appComponent: AppComponent
    }


    override fun onCreate() {
        super.onCreate()

        // Orhanobut logging
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
            .methodCount(2)         // (Optional) How many method line to show. Default 2
            .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
            .tag("3PT")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        // Initialize appcomponent
        appComponent = DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(this))
            .networkModule(NetworkModule(this))
            .build()
    }



}