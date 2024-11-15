package com.viswa.fitfusion.ui


import OnboardingScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.viswa.fitfusion.ui.navigation.TopLevelRoute
import com.viswa.fitfusion.ui.screens.exercise.WorkoutScreen
import com.viswa.fitfusion.ui.screens.plan.PlansScreen
import com.viswa.fitfusion.ui.screens.profile.ProfileScreen
import com.viswa.fitfusion.ui.screens.progress.ProgressScreen
import com.viswa.fitfusion.utils.calculations.AnimationUtil
import com.viswa.fitfusion.utils.calculations.ScaleTransitionDirection

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    topLevelRoute: List<TopLevelRoute<*>>
) {
    val navController = rememberNavController()
    val animationUtils = AnimationUtil()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val bottomNavRoutes = remember { topLevelRoute.map { it.route.toString() } }

            // Conditionally display the bottom navigation bar
            if (currentRoute in bottomNavRoutes) {
                NavigationBar {
                    val currentDestination = navBackStackEntry!!.destination
                    topLevelRoute.forEach { topLevelRoute ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    topLevelRoute.icon,
                                    contentDescription = topLevelRoute.name
                                )
                            },
                            label = {
                                Text(
                                    text = topLevelRoute.name,
                                    maxLines = 1,  // Limit text to a single line
                                    overflow = TextOverflow.Ellipsis // Show ellipsis for overflow
                                )
                            },
                            selected = currentDestination?.hierarchy?.any {
                                // Compare the route class directly
                                it.route == topLevelRoute.route.toString()
                            } == true,
                            onClick = {
                                // Convert T to string when navigating
                                navController.navigate(topLevelRoute.route.toString()) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "onboarding",
            modifier = modifier.padding(innerPadding)
        ) {
            // Define your routes here using `route.toString()`
            composable(
                "onboarding",
                enterTransition = {
                    animationUtils.scaleIntoContainer()
                },
                exitTransition = {
                    animationUtils.scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
                },
                popEnterTransition = {
                    animationUtils.scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
                },
                popExitTransition = {
                    animationUtils.scaleOutOfContainer()
                }
            ) {
                OnboardingScreen(navController)
            }
            composable(
                "workout",
                enterTransition = {
                    animationUtils.scaleIntoContainer()
                },
                exitTransition = {
                    animationUtils.scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
                },
                popEnterTransition = {
                    animationUtils.scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
                },
                popExitTransition = {
                    animationUtils.scaleOutOfContainer()
                }
            ) {
                WorkoutScreen(navController)
            }
            composable(
                "plans",
                enterTransition = {
                    animationUtils.scaleIntoContainer()
                },
                exitTransition = {
                    animationUtils.scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
                },
                popEnterTransition = {
                    animationUtils.scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
                },
                popExitTransition = {
                    animationUtils.scaleOutOfContainer()
                }
            ) {
                PlansScreen(navController)
            }
            composable(
                "progress",
                enterTransition = {
                    animationUtils.scaleIntoContainer()
                },
                exitTransition = {
                    animationUtils.scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
                },
                popEnterTransition = {
                    animationUtils.scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
                },
                popExitTransition = {
                    animationUtils.scaleOutOfContainer()
                }
            ) {
                ProgressScreen(navController)
            }
            composable(
                "profile",
                enterTransition = {
                    animationUtils.scaleIntoContainer()
                },
                exitTransition = {
                    animationUtils.scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
                },
                popEnterTransition = {
                    animationUtils.scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
                },
                popExitTransition = {
                    animationUtils.scaleOutOfContainer()
                }
            ) {
                ProfileScreen(navController)
            }
        }
    }
}
