package com.viswa.fitfusion.data.repository

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Extension property to get the DataStore instance
val Context.dataStore by preferencesDataStore(name = "fitfusion_prefs")
