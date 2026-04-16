package com.anikaitgupta.weatherlauncher.di

import com.anikaitgupta.weatherlauncher.data.network.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun getRetrofitInstance(): Retrofit {
        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
        }.build()

        val baseUrl =
            "https://api.openweathermap.org/"

        /**
         * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
         */
         val retroJson = Json { ignoreUnknownKeys = true }
         val retrofit = Retrofit.Builder()
            .addConverterFactory(retroJson.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
             .client(client)
            .build()
        return retrofit

    }
    @Singleton
    @Provides
    fun getWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}