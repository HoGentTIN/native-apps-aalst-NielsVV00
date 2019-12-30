package com.example.project3pt.fragments.wedstrijdLijst

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WedstrijdLijstViewModelFactory(private val isMijnWedstrijden: Boolean)
    : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WedstrijdLijstViewModel::class.java)) {
                return WedstrijdLijstViewModel(isMijnWedstrijden) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}