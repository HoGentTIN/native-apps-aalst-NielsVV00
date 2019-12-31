package com.example.project3pt.fragments.wedstrijdLijst

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.WedstrijdRepository
import kotlinx.coroutines.launch

class WedstrijdLijstViewModel(
    private val wedstrijdRepository: WedstrijdRepository
) : ViewModel() {

    private var isMijnWedstrijden: Boolean = false

    private val _wedstrijden = MutableLiveData<List<Wedstrijd>>()
    val wedstrijden: LiveData<List<Wedstrijd>>
        get() = _wedstrijden

    private val _hasWedstrijden = MutableLiveData<Boolean>()
    val hasWedstrijden: LiveData<Boolean>
        get() = _hasWedstrijden

    fun isMijnWedstrijden() {
        isMijnWedstrijden = true
    }

    fun getWedstrijden() {
        viewModelScope.launch {
            if (isMijnWedstrijden) {
                _wedstrijden.value = wedstrijdRepository.getMijnWedstrijden()
                _hasWedstrijden.value = hasWedstrijden()
            } else {
                _wedstrijden.value = wedstrijdRepository.refreshWedstrijden()
            }
        }
    }

    fun resetWedstrijden() {
        _wedstrijden.value = listOf()
    }

    /* Implementatie voor keuze fiets (met onclicklistener) */
    private val _navigateToWedstrijd = MutableLiveData<Long>()
    val navigateToWedstrijd
        get() = _navigateToWedstrijd

    fun onWedstrijdClicked(wedstrijdId: Long) {
        Log.i("clicked", wedstrijdId.toString())
        _navigateToWedstrijd.value = wedstrijdId
    }

    fun onWedstrijdNavigated() {
        _navigateToWedstrijd.value = null
    }

    fun logout() {
        wedstrijdRepository.logout()
        wedstrijdRepository.saveMijnWedstrijden(listOf())
    }

    fun hasWedstrijden(): Boolean {
        return !wedstrijdRepository.getMijnWedstrijdenFromShared().isNullOrEmpty()
    }
}
