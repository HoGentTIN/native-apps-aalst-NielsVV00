package com.example.project3pt.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.models.Foto
import com.example.project3pt.repositories.FotoRepository
import com.example.project3pt.repositories.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private var userRepository: UserRepository,
    private var fotoRepository: FotoRepository
) : ViewModel() {

    private val _foto = MutableLiveData<Foto>()
    val foto: LiveData<Foto>
        get() = _foto

    fun init() {
        viewModelScope.launch {
            _foto.value = fotoRepository.getRandom()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
