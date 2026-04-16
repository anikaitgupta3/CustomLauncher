package com.anikaitgupta.weatherlauncher.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

interface WeatherRepository {
    fun getWeatherResult(cityName: String,appid: String): Flow<WeatherResult>
}


class WeatherRepositoryImpl @Inject constructor(private val weatherApi: WeatherApi) : WeatherRepository {
    companion object {
        private const val UNITS = "metric"
        private const val FORECAST_DAYS = 7
    }

    override fun getWeatherResult(
        cityName: String,
        appid: String
    ): Flow<WeatherResult> = flow {
        emit(WeatherResult.Loading)

        try {
            //Log.d("WeatherRepositoryImpl", "Fetching weather data for city: $cityName")
            // 1. Get Geocoding data
            val geoResponse = weatherApi.getGeocoding(cityName, appid)
            val geoBody = geoResponse.body()
            //Log.e("WeatherRepositoryImpl", "Error Code: ${geoResponse.code()} Error Body: ${geoResponse.errorBody()?.string()}")


            if (!geoResponse.isSuccessful || geoBody.isNullOrEmpty()) {
                emit(WeatherResult.Error("Location not found for: $cityName"))
                return@flow
            }

            val lat = geoBody[0].lat
            val lon = geoBody[0].lon

            // 2. Get Current Weather
            val currentResponse = weatherApi.getCurrentWeather(lat, lon, appid, UNITS)
            val currentBody = currentResponse.body()

            if (!currentResponse.isSuccessful || currentBody == null) {
                emit(WeatherResult.Error("Failed to fetch current weather"))
                return@flow
            }

//            // 3. Get Forecast
//            val forecastResponse = weatherApi.getForecast(lat, lon, appid, UNITS, FORECAST_DAYS)
//            val forecastBody = forecastResponse.body()
//            Log.d("WeatherRepositoryImpl", "Forecast response: $forecastBody")
//
//
//            if (!forecastResponse.isSuccessful || forecastBody == null) {
//                emit(WeatherResult.Error("Failed to fetch weather forecast"))
//                return@flow
//            }

            // 4. Map to Domain Model
            val requiredData = RequiredData(
                currentTemp = currentBody.main.temp,
                currentState = currentBody.weather.firstOrNull()?.main ?: "Unknown",
                currentWindSpeed = currentBody.wind.speed,
                currentHumidity = currentBody.main.humidity,
                currentRain = currentBody.clouds.all,
                currentIcon = currentBody.weather.firstOrNull()?.icon ?: "",
//                forecastTempMin = forecastBody.list.map { it.temp.min },
//                forecastTempMax = forecastBody.list.map { it.temp.max },
//                forecastState = forecastBody.list.map { it.weather.firstOrNull()?.main ?: "Unknown" },
//                forecastIcon = forecastBody.list.map { it.weather.firstOrNull()?.icon ?: "" }
            )
            //Log.d("WeatherRepositoryImpl", "Mapped data: $requiredData")


            emit(WeatherResult.Success(requiredData))

        } catch (e: IOException) {
            emit(WeatherResult.Error("Network failure: Please check your connection"))
        } catch (e: Exception) {
            emit(WeatherResult.Error("An unexpected error occurred: ${e.localizedMessage}"))
        }
    }
}
//class WeatherRepositoryImpl(private val weatherApi: WeatherApi) : WeatherRepository {
//    override fun getWeatherResult(
//        cityName: String,
//        appid: String
//    ): Flow<WeatherResult> = flow {
//        emit(WeatherResult.Loading)
//        try {
//            val response = weatherApi.getGeocoding(cityName, appid)
//            if (response.isSuccessful) {
//                val geocodingResult = response.body()
//                if (geocodingResult != null) {
//                    val lat = geocodingResult[0].lat
//                    val lon = geocodingResult[0].lon
//                    val currentWeatherResponse =
//                        weatherApi.getCurrentWeather(lat, lon, appid, "metric")
//                    if (currentWeatherResponse.isSuccessful) {
//                        val currentWeatherResult = currentWeatherResponse.body()
//                        if (currentWeatherResult != null) {
//                            val forecastResponse =
//                                weatherApi.getForecast(lat, lon, appid, "metric", 5)
//                            if (forecastResponse.isSuccessful) {
//                                val forecastResult = forecastResponse.body()
//                                if (forecastResult != null) {
//                                    val requiredData = RequiredData(
//                                        currentWeatherResult.main.temp,
//                                        currentWeatherResult.weather[0].main,
//                                        currentWeatherResult.wind.speed,
//                                        currentWeatherResult.main.humidity,
//                                        currentWeatherResult.clouds.all,
//                                        currentWeatherResult.weather[0].icon,
//                                        forecastResult.list.map { it.temp.min },
//                                        forecastResult.list.map { it.temp.max },
//                                        forecastResult.list.map { it.weather[0].main },
//                                        forecastResult.list.map { it.weather[0].icon }
//                                    )
//                                    emit(WeatherResult.Success(requiredData))
//                                } else {
//                                    emit(WeatherResult.Error("Error"))
//                                }
//                            }
//                        } else {
//                            emit(WeatherResult.Error("Error"))
//                        }
//                    } else {
//                        emit(WeatherResult.Error("Error"))
//                    }
//                } else {
//                    emit(WeatherResult.Error("Error"))
//                }
//            } else {
//                emit(WeatherResult.Error("Error"))
//            }
//        } catch (e: Exception) {
//            emit(WeatherResult.Error("Error"))
//
//
//        }
//    }
//
//}