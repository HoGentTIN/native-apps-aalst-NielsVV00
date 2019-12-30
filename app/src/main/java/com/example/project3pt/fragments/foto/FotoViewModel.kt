package com.example.project3pt.fragments.foto

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.App
import com.example.project3pt.models.Foto
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.IFotoRepository
import com.example.project3pt.repositories.IWedstrijdRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FotoViewModel: ViewModel() {

    @Inject
    lateinit var fotoRepository: IFotoRepository

    private val _fotos = MutableLiveData<List<Foto>>()
    val fotos: LiveData<List<Foto>>
        get() = _fotos

    init {
        App.appComponent.inject(this)
    }

    fun getFotos(){
        viewModelScope.launch {
            _fotos.value = fotoRepository.getAll()
        }
    }

    fun resetFotos() {
        _fotos.value = null
    }
}