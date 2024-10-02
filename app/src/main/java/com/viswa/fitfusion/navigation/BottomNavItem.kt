package com.viswa.fitfusion.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(var route: String, var icon: ImageVector, var title: String) {
    data object Exercise : BottomNavItem("exercise", Icons.Outlined.FitnessCenter, "Exercise")
    data object Plans : BottomNavItem("plans", Icons.Outlined.Checklist, "Plans")
    data object Log : BottomNavItem("log", Icons.Outlined.BarChart, "Log")
    data object Profile : BottomNavItem("profile", Icons.Outlined.Checklist, "Profile")

}