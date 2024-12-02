package com.viswa.fitfusion.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoute = listOf(
    TopLevelRoute("Workout", "workout", Icons.Outlined.FitnessCenter),
    TopLevelRoute("Plans", "plans", Icons.Outlined.Checklist),
    TopLevelRoute("Progress", "progress", Icons.Outlined.BarChart),
    TopLevelRoute("Profile", "profile", Icons.Outlined.Person)
)

object OnboardingRoutes {
    const val ONBOARDING = "onboarding"
    const val GENDER = "gender"
    const val AGE_PICKER = "age_picker"
    // Add other onboarding routes if necessary
}
