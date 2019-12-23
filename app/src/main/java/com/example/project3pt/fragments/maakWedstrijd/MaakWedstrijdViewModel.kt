package com.example.project3pt.fragments.maakWedstrijd

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.App
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.repositories.IWedstrijdRepository
import com.example.project3pt.utils.getShortDateString
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class MaakWedstrijdViewModel : ViewModel(){

    @Inject
    lateinit var wedstrijdRepository: IWedstrijdRepository

    init{
        App.appComponent.inject(this)
    }

    private val _postSucces = MutableLiveData<Boolean>()
    val postSucces: LiveData<Boolean>
    get() = _postSucces

    var datum: Date = Date(Calendar.getInstance().timeInMillis)

    fun saveDate(jaar: Int, maand: Int, dag: Int): String {
        Log.i("jaar", jaar.toString())
        Log.i("now?", datum.toString())
        val calendar = Calendar.getInstance()
        calendar.set(jaar, maand, dag, 0, 0, 0)
        datum = Date(calendar.timeInMillis)
        return getShortDateString(datum)
    }

    fun postWedstrijd(soort: String, plaats: String){
        viewModelScope.launch {
            _postSucces.value = wedstrijdRepository.postWedstrijd(Wedstrijd(datum, soort, plaats))
        }
    }


}