package com.example.customlauncher.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customlauncher.BuildConfig
import com.example.customlauncher.data.network.RequiredData
import com.example.customlauncher.data.network.WeatherRepository
import com.example.customlauncher.data.network.WeatherResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface WeatherUiState{
    object Initial: WeatherUiState
    data class Success(val data: RequiredData): WeatherUiState
    data class Error(val message: String): WeatherUiState
    object Loading: WeatherUiState
}
@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val enteredCity = MutableStateFlow("")
    val _enteredCity: StateFlow<String> = enteredCity.asStateFlow()

    private val textToShowInTextView = MutableStateFlow("")
    val _textToShowInTextView: StateFlow<String> = textToShowInTextView.asStateFlow()

    val weatherScreenState = MutableStateFlow(0)
    val _weatherScreenState: StateFlow<Int> = weatherScreenState.asStateFlow()

    val showProgress = MutableStateFlow(false)
    val _showProgress: StateFlow<Boolean> = showProgress.asStateFlow()




    fun onCityEntered(city: String) {
        enteredCity.value = city
    }
    fun updateText(text: String) {
        textToShowInTextView.value = text
    }
    fun updateWeatherScreenState(state: Int) {
        weatherScreenState.value = state
    }
    fun updateShowProgress(state: Boolean) {
        showProgress.value = state
    }
    fun updateUiState(state: WeatherUiState) {
        _uiState.value = state
    }


    fun getWeather(){
        viewModelScope.launch {
            weatherRepository.getWeatherResult(
                enteredCity.value,
                BuildConfig.API_KEY
            ).collect {
                when (it) {
                    is WeatherResult.Success -> _uiState.value = WeatherUiState.Success(it.data)
                    is WeatherResult.Error -> _uiState.value = WeatherUiState.Error(it.message)
                    is WeatherResult.Loading -> _uiState.value = WeatherUiState.Loading
                }
            }
        }
    }
}