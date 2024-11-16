// File: UtilModule.kt
package com.viswa.fitfusion.di

import com.viswa.fitfusion.utils.calculations.AnimationUtil
import com.viswa.fitfusion.utils.calculations.CalculationUtils
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val utilModule = module {
    singleOf(::CalculationUtils)
    singleOf(::AnimationUtil)
}
