package com.anikaitgupta.weatherlauncher.presentation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anikaitgupta.weatherlauncher.isMyLauncherDefault

@Composable
fun WeatherScreen(onDraggedLeft: () -> Unit, modifier: Modifier){
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    val weatherUiState = weatherViewModel.uiState.collectAsStateWithLifecycle().value
    val enteredCity = weatherViewModel._enteredCity.collectAsStateWithLifecycle().value
    val textToShowInTextView = weatherViewModel._textToShowInTextView.collectAsStateWithLifecycle().value
    val weatherScreenState = weatherViewModel._weatherScreenState.collectAsStateWithLifecycle().value
    val showProgress = weatherViewModel._showProgress.collectAsStateWithLifecycle().value

    // REACTIVE LOGIC: Whenever weatherUiState changes, this block runs
    LaunchedEffect(weatherUiState) {
        when (weatherUiState) {
            WeatherUiState.Initial -> {
                weatherViewModel.updateText("")
                weatherViewModel.updateWeatherScreenState(0)
                weatherViewModel.updateShowProgress(false)
            }
            is WeatherUiState.Loading -> {
                weatherViewModel.updateShowProgress(true)
            }
            is WeatherUiState.Success -> {
                weatherViewModel.updateShowProgress(false)
                weatherViewModel.updateText("")
                weatherViewModel.updateWeatherScreenState(1) // Switches the screen
            }
            is WeatherUiState.Error -> {
                weatherViewModel.updateShowProgress(false)
                weatherViewModel.updateText((weatherUiState as WeatherUiState.Error).message)
            }
        }
    }
    if(weatherScreenState == 0) {
            LocationSearchScreen(modifier,{ weatherViewModel.onCityEntered(it) }, enteredCity, {
                if (enteredCity.isEmpty()) {
                    weatherViewModel.updateText("Enter a city")
                } else {
                    weatherViewModel.updateText("")
                    weatherViewModel.getWeather()
//                    if (weatherUiState is WeatherUiState.Loading)
//                        weatherViewModel.updateShowProgress(true)
//                     if (weatherUiState is WeatherUiState.Error) {
//                        weatherViewModel.updateShowProgress(false)
//                        weatherViewModel.updateText(weatherUiState.message)
//                    }
//                    if (weatherUiState is WeatherUiState.Success) { //WeatherDetailScreen(enteredCity, weatherUiState.data)
//                        weatherViewModel.updateText("")
//                        //weatherViewModel.onCityEntered("")
//                        weatherViewModel.updateWeatherScreenState(1)
//                        weatherViewModel.updateShowProgress(false)
//                    }
                }
            }, textToShowInTextView, showProgress,{
                onDraggedLeft()
                weatherViewModel.updateWeatherScreenState(0)
                weatherViewModel.updateUiState(WeatherUiState.Initial)
            }, isMyLauncherDefault(LocalContext.current) )
        }
    else if(weatherScreenState == 1){
        WeatherDetailScreen(modifier,enteredCity, (weatherUiState as WeatherUiState.Success).data,{
            weatherViewModel.updateWeatherScreenState(0)
            weatherViewModel.onCityEntered("")
            weatherViewModel.updateUiState(WeatherUiState.Initial)
        })
    }

}
