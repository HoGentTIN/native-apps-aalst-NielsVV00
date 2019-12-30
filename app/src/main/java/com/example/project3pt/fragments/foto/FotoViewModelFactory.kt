package com.example.project3pt.fragments.foto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project3pt.models.Foto

class FotoViewModelFactory
    : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FotoViewModel::class.java)) {
                return FotoViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}