package com.viswa.fitfusion.di

import com.viswa.fitfusion.utils.calculations.CalculationUtils
import com.viswa.fitfusion.viewmodel.fitnessviewmodel.FitnessViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Declare the ViewModel for injection
    single { CalculationUtils() }

    // Inject CalculationUtils into FitnessViewModel
    viewModel { FitnessViewModel(get()) }
}
