package com.viswa.fitfusion.data.model

data class UserProfile(
    val userId: String = "",
    val name: String = "",
    val age: Int = 0,
    val height: Int = 0,
    val weight: Int = 0, // Changed from Float to Int
    val activityLevel: String = "",
    val goal: String = "",
    val gender: String = ""
)
