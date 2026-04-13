package com.example.customlauncher.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WeatherScreen(onDraggedLeft:()-> Unit){
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
            LocationSearchScreen({ weatherViewModel.onCityEntered(it) }, enteredCity, {
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
            })
        }
    else if(weatherScreenState == 1){
        WeatherDetailScreen(enteredCity, (weatherUiState as WeatherUiState.Success).data,{
            weatherViewModel.updateWeatherScreenState(0)
            weatherViewModel.onCityEntered("")
        })
    }

}
