package com.example.customlauncher.data.network.geocodingapiresponse

import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResultItem(
    //val country: String,
    val lat: Double,
    //val local_names: LocalNames,
    val lon: Double,
    //val name: String,
    //val state: String
)