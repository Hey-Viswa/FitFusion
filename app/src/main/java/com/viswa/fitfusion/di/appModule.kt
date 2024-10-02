package com.viswa.fitfusion.di

import com.viswa.fitfusion.viewmodel.FitnessViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Declare the ViewModel for injection
    viewModel { FitnessViewModel() }
}
