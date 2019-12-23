package com.example.project3pt.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project3pt.App
import com.example.project3pt.repositories.IUserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel : ViewModel(){

    @Inject
    lateinit var userRepository: IUserRepository

    init {
        App.appComponent.inject(this)
    }

    fun logout(){
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}