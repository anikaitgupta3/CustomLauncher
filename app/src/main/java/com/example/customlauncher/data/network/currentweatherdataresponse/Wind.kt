package com.example.customlauncher.data.network.currentweatherdataresponse

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class Wind(
    //val deg: Int,
   // val gust: Double,
    val speed: Double
)