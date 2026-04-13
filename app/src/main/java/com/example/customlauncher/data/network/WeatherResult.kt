package com.example.customlauncher.data.network

sealed interface WeatherResult{
    data class Success(val data: RequiredData): WeatherResult
    data class Error(val message: String): WeatherResult
    object Loading: WeatherResult
}