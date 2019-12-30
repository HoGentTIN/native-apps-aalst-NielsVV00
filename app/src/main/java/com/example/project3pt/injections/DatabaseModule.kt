package com.example.project3pt.injections

import android.content.Context
import com.example.project3pt.repositories.*
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

    @Provides
    @Singleton
    internal fun provideFotoRepository() : IFotoRepository {
        return FotoRepository(context)
    }
}
