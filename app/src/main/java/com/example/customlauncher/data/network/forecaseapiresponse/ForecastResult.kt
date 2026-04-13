package com.example.customlauncher.data.network.forecaseapiresponse

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResult(
    //val city: City,
    //val cnt: Int,
    //val cod: String,
    val list: List<Item0>,
    //val message: Double
)