package com.anikaitgupta.weatherlauncher.data.network.geocodingapiresponse

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class GeocodingResultItem(
    //val country: String,
    val lat: Double,
    //val local_names: LocalNames,
    val lon: Double,
    //val name: String,
    //val state: String
)