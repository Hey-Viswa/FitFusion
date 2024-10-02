package com.viswa.fitfusion.viewmodel.fitnessviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FitnessViewModel : ViewModel() {

    private val _stamina = MutableLiveData<Int>()
    val stamina: LiveData<Int> = _stamina

    private val _strength = MutableLiveData<Int>()
    val strength: LiveData<Int> = _strength

    private val _flexibility = MutableLiveData<Int>()
    val flexibility: LiveData<Int> = _flexibility

    private val _endurance = MutableLiveData<Int>()
    val endurance: LiveData<Int> = _endurance

    fun calculateStamina(duration: Int, heartRateRecovery: Int) {
        _stamina.value = when {
            duration >= 30 && heartRateRecovery > 20 -> 90
            duration >= 20 && heartRateRecovery > 15 -> 70
            else -> 50
        }
    }

    fun calculateStrength(weightLifted: Double, repetitions: Int) {
        _strength.value = when {
            weightLifted >= 100 && repetitions >= 10 -> 90
            weightLifted >= 70 && repetitions >= 8 -> 70
            else -> 50
        }
    }

    fun calculateFlexibility(reachDistance: Double) {
        _flexibility.value = when {
            reachDistance >= 20 -> 90
            reachDistance >= 10 -> 70
            else -> 50
        }
    }

    fun calculateEndurance(distanceCovered: Double, timeSpent: Int) {
        _endurance.value = when {
            distanceCovered >= 10 && timeSpent >= 60 -> 90
            distanceCovered >= 5 && timeSpent >= 30 -> 70
            else -> 50
        }
    }
}
