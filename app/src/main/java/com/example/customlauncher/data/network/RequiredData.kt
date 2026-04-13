package com.example.customlauncher.data.network

data class RequiredData(
    val currentTemp: Double,
    val currentState: String,
    val currentWindSpeed: Double,
    val currentHumidity: Int,
    val currentRain: Int,
    val currentIcon: String,
    //val forecastTempMin: List<Double>,
    //val forecastTempMax: List<Double>,
    //val forecastState: List<String>,
    //val forecastIcon: List<String>
)