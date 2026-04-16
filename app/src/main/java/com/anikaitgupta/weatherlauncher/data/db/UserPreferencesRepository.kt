package com.anikaitgupta.weatherlauncher.data.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val SHOW_DISCLOSURE = booleanPreferencesKey("show_disclosure")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveCurrentPreference(currentPreference: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_DISCLOSURE] = currentPreference
        }
    }
    val currentPreference: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_DISCLOSURE] ?: false
    }.catch {
        emit(false)
    }
}