package com.anikaitgupta.weatherlauncher.data.network

import com.anikaitgupta.weatherlauncher.data.network.currentweatherdataresponse.CurrentWeatherResult
import com.anikaitgupta.weatherlauncher.data.network.geocodingapiresponse.GeocodingResultItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("geo/1.0/direct")
    suspend fun getGeocoding(
        @Query("q") city: String,
        @Query("appid") appid: String): Response<List<GeocodingResultItem>> // Use List here
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Response<CurrentWeatherResult>
//    @GET("data/2.5/forecast/daily")
//    suspend fun getForecast(
//        @Query("lat") lat: Double,
//        @Query("lon") lon: Double,
//        @Query("appid") appid: String,
//        @Query("units") units: String,
//        @Query("cnt") cnt: Int
//    ): Response<ForecastResult>
}
