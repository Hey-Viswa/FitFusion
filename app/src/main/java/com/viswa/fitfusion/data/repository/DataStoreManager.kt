import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Rename the DataStore extension to avoid ambiguity
private val Context.fusionDataStore by preferencesDataStore(name = "fitfusion_prefs")

class DataStoreManager(context: Context) {
    private val dataStore = context.fusionDataStore

    // Keys
    private val ONBOARDING_COMPLETE_KEY = booleanPreferencesKey("is_onboarding_complete")
    private val USER_ID_KEY = stringPreferencesKey("user_id") // Key for user ID

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

    // Function to save the user ID
    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // Function to get the user ID
    suspend fun getUserId(): String? {
        return dataStore.data
            .map { preferences -> preferences[USER_ID_KEY] }
            .first() // Returns the first emitted value (which is the user ID)
    }
}
