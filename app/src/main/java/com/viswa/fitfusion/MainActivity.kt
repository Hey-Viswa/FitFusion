package com.viswa.fitfusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.viswa.fitfusion.ui.MainScreen
import com.viswa.fitfusion.ui.navigation.topLevelRoute
import com.viswa.fitfusion.ui.theme.FitFusionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitFusionTheme {
                MainScreen(topLevelRoute = topLevelRoute)
            }
        }
    }
}

