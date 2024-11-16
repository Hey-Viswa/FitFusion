package com.viswa.fitfusion.di

import UserRepository
import com.viswa.fitfusion.data.repository.DataStoreManager
import org.koin.dsl.module


val appModule = module {
    single { DataStoreManager(get()) }
    single { UserRepository(get()) }
}
