package com.viswa.fitfusion.ui

import AgePickerScreen
import GenderSelectionScreen
import OnboardingScreen
import UserRepository

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.viswa.fitfusion.data.repository.DataStoreManager
import com.viswa.fitfusion.ui.navigation.OnboardingRoutes
import com.viswa.fitfusion.ui.navigation.TopLevelRoute
import com.viswa.fitfusion.ui.navigation.topLevelRoute
import com.viswa.fitfusion.ui.screens.exercise.WorkoutScreen
import com.viswa.fitfusion.ui.screens.plan.PlansScreen
import com.viswa.fitfusion.ui.screens.profile.ProfileScreen
import com.viswa.fitfusion.ui.screens.progress.ProgressScreen
import com.viswa.fitfusion.ui.theme.Transparent
import com.viswa.fitfusion.utils.calculations.AnimationUtil
import com.viswa.fitfusion.viewmodel.OnboardingViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val userRepository = remember { UserRepository(dataStoreManager) }
    val viewModelFactory = remember { OnboardingViewModelFactory(userRepository) }
    val onboardingViewModel: OnboardingViewModel = viewModel(
        factory = viewModelFactory
    )
    val isOnboardingComplete by dataStoreManager.isOnboardingComplete.collectAsState(initial = false)

    MainNavHost(
        navController = navController,
        modifier = modifier,
        onboardingViewModel = onboardingViewModel,
        isOnboardingComplete = isOnboardingComplete
    )
}
@Composable
fun DisplayOnboardingScreen(
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel
) {
    OnboardingScreen(
        onComplete = {
            onboardingViewModel.completeOnboarding()
        },
        navController = navController,
        titleFontSize = 28f,
        descriptionFontSize = 18f,
        buttonStyle = { text: String, onClick: () -> Unit ->
            Button(onClick = onClick) { Text(text = text) }
        }
    )
}

@Composable
fun MainAppScaffold(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val bottomNavRoutes = topLevelRoute.map { it.route.toString() }

            if (currentRoute in bottomNavRoutes) {
                MainBottomNavigationBar(navController)
            }
        },
        content = content
    )
}
@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel,
    isOnboardingComplete: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isOnboardingComplete) topLevelRoute.first().route.toString() else OnboardingRoutes.ONBOARDING,
        modifier = modifier
    ) {
        // Onboarding Screen
        composable(OnboardingRoutes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    // Navigate to Gender Selection Screen
                    navController.navigate(OnboardingRoutes.GENDER) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                titleFontSize = 28f,
                descriptionFontSize = 18f,
                buttonStyle = { text: String, onClick: () -> Unit ->
                    Button(onClick = onClick) { Text(text = text) }
                },
                navController = navController
                // Removed navController parameter as it's not used in OnboardingScreen
            )
        }

        // Gender Selection Screen
        // MainNavHost function where you use the GenderSelectionScreen
        composable(OnboardingRoutes.GENDER) {
            val coroutineScope = rememberCoroutineScope() // Create coroutine scope

            GenderSelectionScreen(
                onNextClicked = {
                    navController.navigate(OnboardingRoutes.AGE_PICKER)
                },
                onboardingViewModel = onboardingViewModel,
                onGenderSelected = { selectedGender ->
                    // Launch a coroutine to call the suspend function
                    coroutineScope.launch {
                        val success = onboardingViewModel.uploadGender(selectedGender)
                        if (success) {
                            // Navigate to the Age Picker screen after a successful upload
                            navController.navigate(OnboardingRoutes.AGE_PICKER)
                        } else {
                            // Handle error if the upload fails
                            // You can add a callback or show a message accordingly
                            println("Failed to upload gender. Please try again.")
                        }
                    }
                }
            )
        }

        // Age Picker Screen
        composable(OnboardingRoutes.AGE_PICKER) {
            AgePickerScreen(
                onNextClicked = {
                    // Mark onboarding as complete
                    onboardingViewModel.completeOnboarding()
                    // Navigate to main app
                    navController.navigate(topLevelRoute.first().route.toString()) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        // Main App Screens
        topLevelRoute.forEach { route ->
            composable(route.route.toString()) {
                MainAppScaffold(
                    navController = navController,
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            DisplayComposableContent(route, navController)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainBottomNavigationBar(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomNavRoutes = topLevelRoute.map { it.route.toString() }

    if (currentRoute in bottomNavRoutes) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            topLevelRoute.forEach { route ->
                NavigationBarItem(
                    icon = {
                        Icon(route.icon, contentDescription = route.name)
                    },
                    label = {
                        Text(
                            text = route.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    selected = navController.isRouteSelected(route),
                    onClick = { navController.navigateToRoute(route) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        indicatorColor = Transparent
                    )
                )
            }
        }
    }
}

private fun NavHostController.isRouteSelected(route: TopLevelRoute<*>): Boolean {
    val currentDestination = this.currentBackStackEntry?.destination
    return currentDestination?.hierarchy?.any { it.route == route.route.toString() } == true
}

private fun NavHostController.navigateToRoute(route: TopLevelRoute<*>) {
    this.navigate(route.route.toString()) {
        popUpTo(this@navigateToRoute.graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun DisplayComposableContent(
    route: TopLevelRoute<*>,
    navController: NavHostController = rememberNavController(),
    animationUtils: AnimationUtil = AnimationUtil()
) {
    when (route.route.toString()) {
        "workout" -> WorkoutScreen(navController)
        "plans" -> PlansScreen(navController)
        "progress" -> ProgressScreen(navController)
        "profile" -> ProfileScreen(navController)
        else -> throw IllegalArgumentException("Unknown route: ${route.route}")
    }
}
