package com.viswa.fitfusion.di

import com.viswa.fitfusion.utils.calculations.AnimationUtil
import com.viswa.fitfusion.utils.calculations.CalculationUtils
import com.viswa.fitfusion.viewmodel.fitnessviewmodel.FitnessViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // Declare singletons for CalculationUtils and AnimationUtil
    singleOf(::CalculationUtils)
    singleOf(::AnimationUtil)

    // Inject FitnessViewModel using viewModelOf, which automatically resolves dependencies
    viewModelOf(::FitnessViewModel)
}
