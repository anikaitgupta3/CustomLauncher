package com.example.customlauncher.data.db

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.customlauncher.presentation.AppBlock

@Entity(tableName = "favorite_apps")
@Keep
data class FavoriteApps(
    @PrimaryKey
    val appBlock: AppBlock
)