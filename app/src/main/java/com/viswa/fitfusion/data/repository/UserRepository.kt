package com.viswa.fitfusion.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
class UserRepository(private val dataStoreManager: DataStoreManager) {

    private val firestore = FirebaseFirestore.getInstance()

    // Function to upload multiple fields to Firestore
    suspend fun uploadUserData(gender: String, age: String, weight: String): Boolean {
        val userId = dataStoreManager.getUserId()  // This is now a suspend call to fetch the user ID

        if (userId != null) {
            val userDocRef = firestore.collection("users").document(userId)
            val userData = mapOf(
                "gender" to gender,
                "age" to age,
                "weight" to weight
            )
            try {
                userDocRef.set(userData).await() // Use .set() to replace data or .update() to modify existing fields
                return true
            } catch (e: Exception) {
                println("Error uploading user data: ${e.message}")
                return false
            }
        } else {
            throw IllegalStateException("User ID is null")
        }
    }

    // Function to save user ID in DataStore
    suspend fun saveUserId(userId: String) {
        dataStoreManager.saveUserId(userId) // You need to implement this method in DataStoreManager
    }

    // Function to mark onboarding as complete
    suspend fun setOnboardingComplete(isComplete: Boolean) {
        dataStoreManager.setOnboardingComplete(isComplete)
    }
}
