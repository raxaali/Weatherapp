package com.example.weatherapp2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.repository.ApiRepository

class ApiViewModelFactory(private val apirepository: ApiRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  APIViewModel(apirepository) as T
    }
}