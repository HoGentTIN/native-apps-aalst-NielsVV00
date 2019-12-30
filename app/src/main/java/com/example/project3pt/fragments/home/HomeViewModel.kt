package com.example.project3pt.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.App
import com.example.project3pt.models.Foto
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.FotoRepository
import com.example.project3pt.repositories.IFotoRepository
import com.example.project3pt.repositories.IUserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel : ViewModel(){

    @Inject
    lateinit var userRepository: IUserRepository

    @Inject
    lateinit var fotoRepository: IFotoRepository

    private val _foto = MutableLiveData<Foto>()
    val foto: LiveData<Foto>
        get() = _foto

    init {
        App.appComponent.inject(this)
        viewModelScope.launch {
            _foto.value = fotoRepository.getRandom()
        }
    }



    fun logout(){
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}