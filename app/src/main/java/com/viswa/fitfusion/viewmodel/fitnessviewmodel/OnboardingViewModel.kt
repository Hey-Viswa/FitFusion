
package com.viswa.fitfusion.viewmodel.fitnessviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.viswa.fitfusion.data.repository.UserRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Function to upload user data (gender, age, weight)
    suspend fun uploadUserData(gender: String, age: String, weight: String): Boolean {
        return try {
            // Upload data and get the result
            val success = userRepository.uploadUserData(gender, age, weight)

            if (success) {
                // After successful upload, save the user ID in DataStore
                val userId = Firebase.auth.currentUser?.uid
                if (userId != null) {
                    userRepository.saveUserId(userId) // Store the user ID in DataStore
                }
            }
            success
        } catch (e: Exception) {
            println("Error uploading user data: ${e.message}")
            false
        }
    }

    // Mark onboarding as complete
    fun completeOnboarding() {
        viewModelScope.launch {
            userRepository.setOnboardingComplete(true)
        }
    }
}