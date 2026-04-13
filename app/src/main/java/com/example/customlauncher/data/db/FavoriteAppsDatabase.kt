package com.example.customlauncher.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FavoriteApps::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavoriteAppsDatabase : RoomDatabase() {
    abstract fun favoriteAppsDao(): FavoriteAppsDao
}