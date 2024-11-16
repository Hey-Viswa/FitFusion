package com.viswa.fitfusion

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.firebase.FirebaseApp
import com.viswa.fitfusion.ui.MainScreen
import com.viswa.fitfusion.ui.theme.FitFusionTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitFusionTheme {
                MainScreen()
            }
        }
    }
}

