package com.example.project3pt.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginSucces = MutableLiveData<Boolean>()
    val loginSucces: LiveData<Boolean>
        get() = _loginSucces

    // Dagger injection
    fun init() {
        _loginSucces.value = false
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (userRepository.login(email, password)) {
                    _loginSucces.value = true
                }
            }
        }
    }

    fun checkIsUserLoggedIn() {
        viewModelScope.launch {
            _loginSucces.value = userRepository.isUserLoggedIn()
        }
    }
}
