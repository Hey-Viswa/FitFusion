package com.viswa.fitfusion.di


import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FitFusionApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin for dependency injection
        startKoin {
            // Provide the application context
            androidContext(this@FitFusionApp)
            // Load Koin modules
            modules(appModule)
        }
    }
}