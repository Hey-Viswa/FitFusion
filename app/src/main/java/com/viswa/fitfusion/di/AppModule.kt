package com.viswa.fitfusion.di

import DataStoreManager
import com.viswa.fitfusion.data.repository.UserRepository

import org.koin.dsl.module


val appModule = module {
    single { DataStoreManager(get()) }
    single { UserRepository(get()) }
}
