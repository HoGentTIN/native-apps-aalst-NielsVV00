package com.example.project3pt.injections

import com.example.project3pt.App
import com.example.project3pt.fragments.foto.FotoViewModel
import com.example.project3pt.fragments.home.HomeViewModel
import com.example.project3pt.fragments.login.LoginViewModel
import com.example.project3pt.fragments.maakWedstrijd.MaakWedstrijdViewModel
import com.example.project3pt.fragments.registreer.RegistreerViewModel
import com.example.project3pt.repositories.UserRepository
import com.example.project3pt.fragments.wedstrijd.WedstrijdViewModel
import com.example.project3pt.fragments.wedstrijdLijst.WedstrijdLijstViewModel
import com.example.project3pt.repositories.FotoRepository
import com.example.project3pt.repositories.WedstrijdRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface AppComponent {

    fun inject(app: App)

    fun inject(wedstrijdRepository: WedstrijdRepository)
    fun inject(userRepository: UserRepository)
    fun inject(fotoRepository: FotoRepository)

    fun inject(registreerViewModel: RegistreerViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(wedstrijdLijstViewModel: WedstrijdLijstViewModel)
    fun inject(wedstrijdViewModel: WedstrijdViewModel)
    fun inject(maakWedstrijdViewModel: MaakWedstrijdViewModel)
    fun inject(fotoViewModel: FotoViewModel)


    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        fun networkModule(networkModule: NetworkModule): Builder
        fun databaseModule(databaseModule: DatabaseModule): Builder
    }
}
