package com.example.customlauncher.data.network.currentweatherdataresponse

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class Clouds(
    val all: Int
)