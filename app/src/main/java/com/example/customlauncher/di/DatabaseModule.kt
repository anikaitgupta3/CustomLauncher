package com.example.customlauncher.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.customlauncher.data.FavoriteAppsDao
import com.example.customlauncher.data.FavoriteAppsDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{
    @Singleton
    @Provides
    fun getDatabase(context: Application): FavoriteAppsDatabase {
        // if the Instance is not null, return it, otherwise create a new database instance.
        return Room.databaseBuilder(context, FavoriteAppsDatabase::class.java, "favorite_apps_database")
            .build()

    }
    @Singleton
    @Provides
    fun getDao(favoriteAppsDatabase: FavoriteAppsDatabase): FavoriteAppsDao{
        return favoriteAppsDatabase.favoriteAppsDao()
    }
}