package com.example.project3pt.fragments.wedstrijd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.WedstrijdRepository
import kotlinx.coroutines.launch

class WedstrijdViewModel(
    private val wedstrijdRepository: WedstrijdRepository
) : ViewModel() {

    private val _wedstrijd = MutableLiveData<Wedstrijd>()
    val wedstrijd: LiveData<Wedstrijd>
        get() = _wedstrijd

    private val _deelnemers = MutableLiveData<List<Deelnemer>>()
    val deelnemers: LiveData<List<Deelnemer>>
        get() = _deelnemers

    var wedstrijdKey: Long = 0

    fun initData(key: Long) {
        wedstrijdKey = key
        setWedstrijd()
    }

    fun setWedstrijd() {
        viewModelScope.launch {
            _wedstrijd.value = wedstrijdRepository.getWedstrijd(wedstrijdKey)
            _deelnemers.value = wedstrijdRepository.refreshDeelnemers(wedstrijdKey)
        }
    }

    fun neemDeel() {
        viewModelScope.launch {
            wedstrijdRepository.neemDeel(wedstrijdKey)
            wedstrijdRepository.addOneToMijnWedstrijden(wedstrijd.value!!)
            _deelnemers.value = wedstrijdRepository.refreshDeelnemers(wedstrijdKey)
        }
    }
}
