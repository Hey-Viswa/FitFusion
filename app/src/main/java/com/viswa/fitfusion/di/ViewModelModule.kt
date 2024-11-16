// File: ViewModelModule.kt
package com.viswa.fitfusion.di



import OnboardingViewModel
import com.viswa.fitfusion.viewmodel.fitnessviewmodel.FitnessViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::FitnessViewModel)
    viewModelOf(::OnboardingViewModel)
}
