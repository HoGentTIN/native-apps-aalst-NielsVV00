package com.example.project3pt.fragments.wedstrijdLijst

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.App
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.IWedstrijdRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class WedstrijdLijstViewModel: ViewModel() {

    @Inject
    lateinit var wedstrijdRepository: IWedstrijdRepository

    private val _wedstrijden = MutableLiveData<List<Wedstrijd>>()
    val wedstrijden: LiveData<List<Wedstrijd>>
        get() = _wedstrijden

    init {
        App.appComponent.inject(this)
    }

    fun getWedstrijden(){
        viewModelScope.launch {
            _wedstrijden.value = wedstrijdRepository.getWedstrijdenFromApi()
        }
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
}