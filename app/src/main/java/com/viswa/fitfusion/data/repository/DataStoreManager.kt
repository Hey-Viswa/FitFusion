package com.viswa.fitfusion.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Rename the DataStore extension to avoid ambiguity
private val Context.fusionDataStore by preferencesDataStore(name = "fitfusion_prefs")

class DataStoreManager(context: Context) {
    private val dataStore = context.fusionDataStore

    // Keys
    private val ONBOARDING_COMPLETE_KEY = booleanPreferencesKey("is_onboarding_complete")

    // Function to update onboarding completion status
    suspend fun setOnboardingComplete(isComplete: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETE_KEY] = isComplete
        }
    }

    // Function to read onboarding completion status
    val isOnboardingComplete: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETE_KEY] ?: false
        }
}
