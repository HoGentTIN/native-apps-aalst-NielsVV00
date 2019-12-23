package com.example.project3pt.injections

import android.content.Context
import com.example.project3pt.repositories.IUserRepository
import com.example.project3pt.repositories.UserRepository
import com.example.project3pt.repositories.IWedstrijdRepository
import com.example.project3pt.repositories.WedstrijdRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context){

    @Provides
    @Singleton
    internal fun provideUserRepository() : IUserRepository {
        return UserRepository(context)
    }

    @Provides
    @Singleton
    internal fun provideWedstrijdRepository() : IWedstrijdRepository {
        return WedstrijdRepository(context)
    }
}
