package com.example.customlauncher.data.db

import com.example.customlauncher.presentation.AppBlock
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteAppsRepository @Inject constructor(private val favoriteAppsDao: FavoriteAppsDao) {
    suspend fun insertFavoriteApp(appBlock: AppBlock){
        favoriteAppsDao.insertFavoriteApp(FavoriteApps(appBlock))
    }
    suspend fun deleteFavoriteApp(appBlock: AppBlock){
        favoriteAppsDao.removeFavoriteApp(appBlock)
    }
    fun getAllFavoriteApps(): Flow<List<FavoriteApps>>{
        return favoriteAppsDao.getAllFavoriteApps()
    }
}