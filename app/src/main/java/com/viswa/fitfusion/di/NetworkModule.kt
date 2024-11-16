// File: NetworkModule.kt
package com.viswa.fitfusion.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(Android) {
            engine {
                connectTimeout = 10_000
                socketTimeout = 10_000
            }
        }
    }
}
