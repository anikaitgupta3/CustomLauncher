package com.anikaitgupta.weatherlauncher.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anikaitgupta.weatherlauncher.data.db.FavoriteAppsRepository
import com.anikaitgupta.weatherlauncher.data.db.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(private val favoriteAppsRepository: FavoriteAppsRepository,private val userPreferencesRepository: UserPreferencesRepository): ViewModel(){
    private val _listOfFavoriteApps = MutableStateFlow<List<AppBlock>>(emptyList())
    val listOfFavoriteApps = _listOfFavoriteApps.asStateFlow()
    private val screenToShow = MutableStateFlow(0)
    val screenToShowFlow = screenToShow.asStateFlow()
    // Read the flow
    val hasSeenDisclosure = userPreferencesRepository.currentPreference.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    // Update the preference
    fun onDisclosureAccepted() {
        viewModelScope.launch {
            userPreferencesRepository.saveCurrentPreference(true)
        }
    }



    init{
        viewModelScope.launch {
            favoriteAppsRepository.getAllFavoriteApps().collect {list->
                _listOfFavoriteApps.value=list.map {
                    it.appBlock
                }
            }
        }
    }
    fun addFavoriteApp(appBlock: AppBlock){
        viewModelScope.launch {
            favoriteAppsRepository.insertFavoriteApp(appBlock)
        }
    }
    fun removeFavoriteApp(appBlock: AppBlock){
        viewModelScope.launch {
            favoriteAppsRepository.deleteFavoriteApp(appBlock)
        }
    }
    fun updateScreenToShow(screen:Int) {
        viewModelScope.launch {
            screenToShow.value = screen
        }
    }

}