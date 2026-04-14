package com.example.customlauncher.data.network.currentweatherdataresponse

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class Weather(
    //val description: String,
    val icon: String,
    //val id: Int,
    val main: String
)