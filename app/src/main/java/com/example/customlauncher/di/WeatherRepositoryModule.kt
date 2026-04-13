package com.example.customlauncher.di

import com.example.customlauncher.data.network.WeatherRepository
import com.example.customlauncher.data.network.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMyRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}