package com.example.customlauncher.data.network.currentweatherdataresponse

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResult(
    //val base: String,
    val clouds: Clouds,
    //val cod: Int,
    //val coord: Coord,
    //aval dt: Int,
    //val id: Int,
    val main: Main,
    //val name: String,
    //val rain: Rain,
    //val sys: Sys,
   // val timezone: Int,
    //val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)