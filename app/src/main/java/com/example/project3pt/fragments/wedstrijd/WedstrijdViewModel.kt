package com.example.project3pt.fragments.wedstrijd

import androidx.lifecycle.*
import com.example.project3pt.App
import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.IWedstrijdRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class WedstrijdViewModel(
    private val wedstrijdKey: Long
//TODO IS LEEG
) : ViewModel(){

    @Inject
    lateinit var wedstrijdRepository: IWedstrijdRepository

    private val _wedstrijd = MutableLiveData<Wedstrijd>()
    val wedstrijd: LiveData<Wedstrijd>
        get() = _wedstrijd

    private val _deelnemers = MutableLiveData<List<Deelnemer>>()
    val deelnemers: LiveData<List<Deelnemer>>
        get() = _deelnemers

    init{
        App.appComponent.inject(this)
        setWedstrijd()
    }

    fun setWedstrijd(){
        viewModelScope.launch {
            _wedstrijd.value = wedstrijdRepository.getWedstrijdFromApi(wedstrijdKey)
            _deelnemers.value = wedstrijdRepository.getDeelnemersFromWedstrijd(wedstrijdKey)
        }
    }

    fun neemDeel() {
        viewModelScope.launch {
            wedstrijdRepository.neemDeel(wedstrijdKey)
            _deelnemers.value = wedstrijdRepository.getDeelnemersFromWedstrijd(wedstrijdKey)
        }
    }


}