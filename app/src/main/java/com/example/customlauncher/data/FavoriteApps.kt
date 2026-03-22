package com.example.customlauncher.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.customlauncher.presentation.AppBlock

@Entity(tableName = "favorite_apps")
data class FavoriteApps(
    @PrimaryKey
    val appBlock: AppBlock
)