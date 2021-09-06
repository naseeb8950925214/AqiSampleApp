package com.example.aqiapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aqiapp.data.repository.AqiRepository
import com.example.aqiapp.ui.main.viewmodel.AqiViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AqiViewModel::class.java) -> {
                return AqiViewModel(AqiRepository()) as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }

}