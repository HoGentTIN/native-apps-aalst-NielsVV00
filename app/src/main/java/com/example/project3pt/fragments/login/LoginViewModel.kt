package com.example.project3pt.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.App
import com.example.project3pt.repositories.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel: ViewModel() {

    @Inject
    lateinit var userRepository: IUserRepository

    private val _loginSucces = MutableLiveData<Boolean>()
    val loginSucces: LiveData<Boolean>
        get() = _loginSucces

    // Dagger injection
    init {
        App.appComponent.inject(this)
        _loginSucces.value = false
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                if(userRepository.login(email, password)){
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