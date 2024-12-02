package com.viswa.fitfusion.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FitFusionApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin with your modules here
        startKoin {
            androidContext(this@FitFusionApp)
            modules(
                listOf(
                    firebaseModule,
                    networkModule,
                    viewModelModule,
                    utilModule,
                    appModule
                )
            )
        }
    }
}
