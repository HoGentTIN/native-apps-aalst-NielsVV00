package com.example.project3pt.fragments.wedstrijdLijst

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.App
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.IUserRepository
import com.example.project3pt.repositories.IWedstrijdRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class WedstrijdLijstViewModel(private val isMijnWedstrijden: Boolean): ViewModel() {

    @Inject
    lateinit var wedstrijdRepository: IWedstrijdRepository

    private val _wedstrijden = MutableLiveData<List<Wedstrijd>>()
    val wedstrijden: LiveData<List<Wedstrijd>>
        get() = _wedstrijden

    private val _hasWedstrijden = MutableLiveData<Boolean>()
    val hasWedstrijden: LiveData<Boolean>
        get() = _hasWedstrijden

    init {
        App.appComponent.inject(this)

    }

    fun getWedstrijden(){
        viewModelScope.launch {
            if(isMijnWedstrijden){
                _wedstrijden.value = wedstrijdRepository.getMijnWedstrijden()
                _hasWedstrijden.value = hasWedstrijden()
            }else{
                _wedstrijden.value = wedstrijdRepository.refreshWedstrijden()
            }
        }
    }

    fun resetWedstrijden(){
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

    fun onWedstrijdNavigated(){
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