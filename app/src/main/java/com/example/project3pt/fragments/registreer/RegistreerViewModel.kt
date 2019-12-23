package com.example.project3pt.fragments.registreer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.App
import com.example.project3pt.repositories.IUserRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class RegistreerViewModel: ViewModel() {

    @Inject
    lateinit var userRepository: IUserRepository

    private val _registerSucces = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean>
        get() = _registerSucces

    init {
        // Inject Services with Dagger
        App.appComponent.inject(this)
        _registerSucces.value = false
    }

    fun register(email: String, password: String, firstname: String, lastname: String) {
        viewModelScope.launch {
            try {
                _registerSucces.value = userRepository.register(email, password, firstname, lastname)
            }catch (e: Exception){
                Log.e("Error", e.message)
            }
        }
    }
}