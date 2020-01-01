package com.example.project3pt.fragments.wedstrijd

import android.util.Log
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

    private val _deelgenomen = MutableLiveData<Boolean>()
    val deelgenomen: LiveData<Boolean>
        get() = _deelgenomen

    var wedstrijdKey: Long = 0

    fun initData(key: Long) {
        wedstrijdKey = key
        setWedstrijd()
    }

    fun setWedstrijd() {
        viewModelScope.launch {
            _wedstrijd.value = wedstrijdRepository.getWedstrijd(wedstrijdKey)
            checkIfDeelgenomen()
            _deelnemers.value = wedstrijdRepository.refreshDeelnemers(wedstrijdKey)
        }
    }

    fun neemDeel() {
        viewModelScope.launch {
            if(!_deelgenomen.value!!){
                wedstrijdRepository.addOneToMijnWedstrijden(wedstrijd.value!!)
            }else if(_deelgenomen.value!!){
                wedstrijdRepository.removeOneFromMijnWedstrijden(wedstrijd.value!!)
            }
            wedstrijdRepository.neemDeel(wedstrijdKey)
            _deelnemers.value = wedstrijdRepository.refreshDeelnemers(wedstrijdKey)
            checkIfDeelgenomen()
        }
    }

    fun checkIfDeelgenomen() {
        viewModelScope.launch {
            val wedstrijden = wedstrijdRepository.getMijnWedstrijden()
            if(!wedstrijden.isNullOrEmpty()){
                val bool = wedstrijden.contains(_wedstrijd.value)
                _deelgenomen.value =  bool
            }else{
                _deelgenomen.value = false
            }
        }
    }
}
