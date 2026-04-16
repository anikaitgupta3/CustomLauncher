package com.anikaitgupta.weatherlauncher.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.anikaitgupta.weatherlauncher.presentation.AppBlock
import kotlinx.coroutines.flow.Flow
@Dao
interface FavoriteAppsDao {
    @Query("select* from favorite_apps")
    fun getAllFavoriteApps(): Flow<List<FavoriteApps>>

    @Query("delete from favorite_apps where appBlock=:appBlock")
    suspend fun removeFavoriteApp(appBlock: AppBlock)

    @Insert(onConflict = IGNORE)
    suspend fun insertFavoriteApp(favoriteApp: FavoriteApps)
}