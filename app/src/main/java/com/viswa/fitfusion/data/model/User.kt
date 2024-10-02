package com.viswa.fitfusion.data.model

data class User(
    val height: Double,           // Height of the user in meters
    val weight: Double,           // Weight of the user in kilograms
    val bmi: Double,              // Body Mass Index
    val bmr: Double,              // Basal Metabolic Rate
    val age: Int,                 // User's age
    val isMale: Boolean,          // Gender boolean (true if male, false if female)
    val name: String,             // User's name
    val email: String,            // User's email for authentication
    val password: String,         // Password for user authentication
    val stamina: Int,             // Stamina metric (e.g., 0 to 100 scale)
    val strength: Int,            // Strength metric (e.g., 0 to 100 scale)
    val flexibility: Int,         // Flexibility metric (e.g., 0 to 100 scale)
    val endurance: Int            // Endurance metric (e.g., 0 to 100 scale)
)
