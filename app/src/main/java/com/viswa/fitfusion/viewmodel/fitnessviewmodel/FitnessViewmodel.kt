package com.viswa.fitfusion.viewmodel.fitnessviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.viswa.fitfusion.utils.calculations.CalculationUtils

class FitnessViewModel(private val calculationUtils: CalculationUtils) : ViewModel() {

    private val _stamina = MutableLiveData<Int>()
    val stamina: LiveData<Int> = _stamina

    private val _strength = MutableLiveData<Int>()
    val strength: LiveData<Int> = _strength

    private val _flexibility = MutableLiveData<Int>()
    val flexibility: LiveData<Int> = _flexibility

    private val _endurance = MutableLiveData<Int>()
    val endurance: LiveData<Int> = _endurance

    // Use CalculationUtils to calculate fitness metrics
    fun calculateStamina(duration: Int, heartRateRecovery: Int) {
        _stamina.value = calculationUtils.calculateStamina(duration, heartRateRecovery)
    }

    fun calculateStrength(weightLifted: Double, repetitions: Int) {
        _strength.value = calculationUtils.calculateStrength(weightLifted, repetitions)
    }

    fun calculateFlexibility(reachDistance: Double) {
        _flexibility.value = calculationUtils.calculateFlexibility(reachDistance)
    }

    fun calculateEndurance(distanceCovered: Double, timeSpent: Int) {
        _endurance.value = calculationUtils.calculateEndurance(distanceCovered, timeSpent)
    }
}
