package com.example.customlauncher.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customlauncher.data.db.FavoriteAppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(private val favoriteAppsRepository: FavoriteAppsRepository): ViewModel(){
    private val _listOfFavoriteApps = MutableStateFlow<List<AppBlock>>(emptyList())
    val listOfFavoriteApps = _listOfFavoriteApps.asStateFlow()
    private val screenToShow = MutableStateFlow(0)
    val screenToShowFlow = screenToShow.asStateFlow()


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