package com.viswa.fitfusion.utils.calculations

class CalculationUtils {

    // BMI Calculation
    fun calculateBMI(height: Double, weight: Double): Double {
        return weight / (height * height)
    }

    // BMR Calculation (Harris-Benedict Formula)
    fun calculateBMR(weight: Double, height: Double, age: Int, isMale: Boolean): Double {
        return if (isMale) {
            88.36 + (13.4 * weight) + (4.8 * height) - (5.7 * age)
        } else {
            447.6 + (9.2 * weight) + (3.1 * height) - (4.3 * age)
        }
    }

    // Stamina Calculation
    fun calculateStamina(duration: Int, heartRateRecovery: Int): Int {
        return when {
            duration >= 30 && heartRateRecovery > 20 -> 90
            duration >= 20 && heartRateRecovery > 15 -> 70
            else -> 50
        }
    }

    // Strength Calculation
    fun calculateStrength(weightLifted: Double, repetitions: Int): Int {
        return when {
            weightLifted >= 100 && repetitions >= 10 -> 90
            weightLifted >= 70 && repetitions >= 8 -> 70
            else -> 50
        }
    }

    // Flexibility Calculation
    fun calculateFlexibility(reachDistance: Double): Int {
        return when {
            reachDistance >= 20 -> 90
            reachDistance >= 10 -> 70
            else -> 50
        }
    }

    // Endurance Calculation
    fun calculateEndurance(distanceCovered: Double, timeSpent: Int): Int {
        return when {
            distanceCovered >= 10 && timeSpent >= 60 -> 90
            distanceCovered >= 5 && timeSpent >= 30 -> 70
            else -> 50
        }
    }
}
