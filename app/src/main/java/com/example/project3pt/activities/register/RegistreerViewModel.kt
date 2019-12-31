package com.example.project3pt.activities.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.repositories.UserRepository
import java.lang.Exception
import kotlinx.coroutines.launch

class RegistreerViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerSucces = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean>
        get() = _registerSucces

    fun init() {
        _registerSucces.value = false
    }

    fun register(email: String, password: String, firstname: String, lastname: String) {
        viewModelScope.launch {
            try {
                _registerSucces.value = userRepository.register(email, password, firstname, lastname)
            } catch (e: Exception) {
                Log.e("Error", e.message)
            }
        }
    }
}
