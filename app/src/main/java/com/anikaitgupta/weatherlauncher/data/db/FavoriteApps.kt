package com.anikaitgupta.weatherlauncher.data.db

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anikaitgupta.weatherlauncher.presentation.AppBlock

@Entity(tableName = "favorite_apps")
@Keep
data class FavoriteApps(
    @PrimaryKey
    val appBlock: AppBlock
)