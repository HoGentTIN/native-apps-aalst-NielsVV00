package com.example.project3pt.fragments.wedstrijd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WedstrijdViewModelFactory(private val wedstrijdKey: Long)
    : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WedstrijdViewModel::class.java)) {
                return WedstrijdViewModel(wedstrijdKey) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}