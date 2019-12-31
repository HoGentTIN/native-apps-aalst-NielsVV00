package com.example.project3pt.fragments.foto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.models.Foto
import com.example.project3pt.repositories.FotoRepository
import kotlinx.coroutines.launch

class FotoViewModel(
    private val fotoRepository: FotoRepository
) : ViewModel() {

    private val _fotos = MutableLiveData<List<Foto>>()
    val fotos: LiveData<List<Foto>>
        get() = _fotos

    fun getFotos() {
        viewModelScope.launch {
            _fotos.value = fotoRepository.getAll()
        }
    }

    fun resetFotos() {
        _fotos.value = null
    }

    fun addFoto(foto: Foto) {
        val list = fotos.value?.toMutableList()
        list?.add(foto)
        _fotos.value = list
    }
}
