package com.viswa.fitfusion.services.firebase.firestore

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.viswa.fitfusion.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.io.IOException

fun logUserProfileSaved(context: Context) {
    val analytics = FirebaseAnalytics.getInstance(context)
    analytics.logEvent("user_profile_saved", null)
}

suspend fun readUserProfileFromFirestore(userId: String): UserProfile? {
    val db = FirebaseFirestore.getInstance()

    return try {
        // Get the document using the user ID
        val documentSnapshot: DocumentSnapshot = db.collection("user_profiles")
            .document(userId)
            .get()
            .await() // suspend function to await result

        if (documentSnapshot.exists()) {
            // Parse the document snapshot into a UserProfile object
            documentSnapshot.toObject(UserProfile::class.java)
        } else {
            println("User profile does not exist for userId: $userId")
            null
        }
    } catch (e: FirebaseFirestoreException) {
        // Handle Firestore-specific errors
        println("Failed to read user profile: ${e.message}")
        e.printStackTrace()
        null
    } catch (e: IOException) {
        // Handle network-related errors
        println("Network error: ${e.message}")
        e.printStackTrace()
        null
    } catch (e: Exception) {
        // Handle other unexpected errors
        println("An unexpected error occurred: ${e.message}")
        e.printStackTrace()
        null
    }
}

fun SaveUserProfileToFirestore(userProfile: UserProfile, context: Context) {
    val db = FirebaseFirestore.getInstance()

    db.collection("user_profiles")
        .document(userProfile.userId) // Use user ID as document ID for easy access
        .set(userProfile, SetOptions.merge()) // SetOptions.merge() allows partial updates
        .addOnSuccessListener {
            // Successfully saved user profile
            // Optional: Log success or notify the user
            println("User profile successfully saved to Firestore")

            // Optional: Navigate to a new activity or fragment
//            navigateToMainScreen()

            // Optional: Log analytics for successful onboarding
            FirebaseAnalytics.getInstance(context).logEvent("user_profile_saved", null)
        }
        .addOnFailureListener { e ->
            // Handle failure
            println("Failed to save user profile: ${e.message}")

            when (e) {
                is FirebaseFirestoreException -> {
                    // Specific handling for Firestore-related exceptions
                    when (e.code) {
                        FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                            println("User does not have permission to write to Firestore.")
                            // Notify the user to check their permissions.
                        }

                        FirebaseFirestoreException.Code.UNAVAILABLE -> {
                            println("Firestore service unavailable, please try again later.")
                            // Maybe show a retry button to try again later.
                        }

                        else -> {
                            println("An unknown Firestore error occurred.")
                        }
                    }
                }

                is IOException -> {
                    println("Network error: Please check your internet connection.")
                    // Suggest user to check their internet connectivity
                }

                else -> {
                    println("An unexpected error occurred: ${e.localizedMessage}")
                    // General error handling
                }
            }

            // Optional: Log analytics for failure
            FirebaseAnalytics.getInstance(context).logEvent("user_profile_save_failed", null)
        }
}

fun getUserProfile(userId: String) {
    CoroutineScope(Dispatchers.IO).launch {
        val userProfile = readUserProfileFromFirestore(userId)
        if (userProfile != null) {
            println("User Profile: $userProfile")
            // You can update your UI or handle the user profile data here
        } else {
            println("User profile not found or an error occurred.")
        }
    }
}