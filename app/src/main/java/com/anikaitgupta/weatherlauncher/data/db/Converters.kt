package com.anikaitgupta.weatherlauncher.data.db

import androidx.room.TypeConverter
import com.anikaitgupta.weatherlauncher.presentation.AppBlock
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromAppBlock(appBlock: AppBlock): String {
        return gson.toJson(appBlock)
    }

    @TypeConverter
    fun toAppBlock(appBlockString: String): AppBlock {
        return gson.fromJson(appBlockString, AppBlock::class.java)
    }
}