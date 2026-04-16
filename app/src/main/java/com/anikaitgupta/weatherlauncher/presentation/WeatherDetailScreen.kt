package com.anikaitgupta.weatherlauncher.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.anikaitgupta.weatherlauncher.R
import com.anikaitgupta.weatherlauncher.data.network.RequiredData
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun WeatherDetailScreen(
    modifier: Modifier,
    cityName: String,
    weatherData: RequiredData,
    onBackClick: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
//    val backgroundRes = remember(hour) {
//        if (hour in 6..17) R.drawable.day_bg else R.drawable.night_bg
//    }
    val backgroundRes = R.drawable.day_bg

    BackHandler {
        onBackClick()
    }

    Box(modifier =modifier.fillMaxSize().verticalScroll(rememberScrollState(),)) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = cityName,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1.3f))
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Current Weather Icon
            //Log.d("TAG", "https://openweathermap.org/payload/api/media/file/${weatherData.currentIcon}.png")
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${weatherData.currentIcon}@4x.png",
                contentDescription = null,
                onError = { error ->
                    Log.e("WeatherIcon", "Error loading image: ${error.result.throwable}")
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Current Temperature
            Text(
                text = "${weatherData.currentTemp.roundToInt()}",
                fontSize = 80.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Current State
            Text(
                text = weatherData.currentState,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Weather Details Surface
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherInfoItem(
                        iconRes = R.drawable.windy,
                        value = "${(weatherData.currentWindSpeed * 3.6).roundToInt()} km/h",
                        label = "Wind"
                    )
                    WeatherInfoItem(
                        iconRes = R.drawable.humidity,
                        value = "${weatherData.currentHumidity}%",
                        label = "Humidity"
                    )
                    WeatherInfoItem(
                        iconRes = R.drawable.cloudy,
                        value = "${weatherData.currentRain}%",
                        label = "Rain"
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

//            Text(
//                text = "7 day forecast",
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))

//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(20.dp)
//            ) {
//                itemsIndexed(weatherData.forecastState) { index, state ->
//                    ForecastRow(
//                        index = index,
//                        state = state,
//                        icon = weatherData.forecastIcon[index],
//                        maxTemp = weatherData.forecastTempMax[index].roundToInt(),
//                        minTemp = weatherData.forecastTempMin[index].roundToInt()
//                    )
//                }
//            }
        }
    }
}

@Composable
fun WeatherInfoItem(iconRes: Int, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Text(text = label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}

//@Composable
//fun ForecastRow(index: Int, state: String, icon: String, maxTemp: Int, minTemp: Int) {
//    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
//    val calendar = Calendar.getInstance()
//    calendar.add(Calendar.DAY_OF_YEAR, index + 1)
//    val dayName = sdf.format(calendar.time)
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = dayName,
//            color = Color.White,
//            fontSize = 16.sp,
//            modifier = Modifier.weight(1f)
//        )
//
//        Row(
//            modifier = Modifier.weight(1f),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Start
//        ) {
//            AsyncImage(
//                model = "https://openweathermap.org/payload/api/media/file/$icon.png",
//                contentDescription = null,
//                modifier = Modifier.size(30.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = state, color = Color.White, fontSize = 16.sp)
//        }
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                text = "$maxTemp",
//                color = Color.White,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = "/$minTemp",
//                color = Color.White.copy(alpha = 0.7f),
//                fontSize = 14.sp
//            )
//        }
//    }
//}

//fun WeatherDetailScreenPreview() {
//    WeatherDetailScreen(
//        modifier = modifier,
//        cityName = "London",
//        weatherData = RequiredData(
//            currentTemp = 25.0,
//            currentState = "Sunny",
//            currentWindSpeed = 5.0,
//            currentHumidity = 60,
//            currentRain = 10,
//            currentIcon = "01d",
////            forecastTempMin = listOf(20.0, 21.0, 22.0, 23.0, 24.0, 25.0, 25.0),
////            forecastTempMax = listOf(25.0, 26.0, 27.0, 28.0, 29.0, 30.0, 30.0),
////            forecastState = listOf("Sunny", "Cloudy", "Rainy", "Sunny", "Cloudy", "Rainy", "Sunny"),
////            forecastIcon = listOf("01d", "02d", "03d", "01d", "02d", "03d", "01d")
//        )
//    ) {}
//
//
//}
