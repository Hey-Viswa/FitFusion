package com.viswa.fitfusion.utils.calculations

fun calculateBMI(weight: Double, height: Double): Double {
    return weight / (height * height)
}

fun calculateBMR(weight: Double, height: Double, age: Int, isMale: Boolean): Double {
    return if (isMale) {
        88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
    } else {
        447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
    }

}

fun calculateStamina(duration: Int, heartRateRecovery: Int): Int {
    // Example: Duration is in minutes, heartRateRecovery is the drop in beats per minute after 1 minute of rest.
    return when {
        duration >= 30 && heartRateRecovery > 20 -> 90 // High stamina
        duration >= 20 && heartRateRecovery > 15 -> 70 // Moderate stamina
        else -> 50 // Low stamina
    }
}

fun calculateStrength(weightLifted: Double, repetitions: Int): Int {
    // Example: Weight lifted in kilograms and repetitions performed.
    return when {
        weightLifted >= 100 && repetitions >= 10 -> 90 // High strength
        weightLifted >= 70 && repetitions >= 8 -> 70 // Moderate strength
        else -> 50 // Low strength
    }
}

fun calculateFlexibility(reachDistance: Double): Int {
    // Example: Reach distance in centimeters (positive beyond toes, negative if can't reach toes).
    return when {
        reachDistance >= 20 -> 90 // High flexibility
        reachDistance >= 10 -> 70 // Moderate flexibility
        else -> 50 // Low flexibility
    }
}

fun calculateEndurance(distanceCovered: Double, timeSpent: Int): Int {
    // Example: Distance covered in kilometers and time spent in minutes.
    return when {
        distanceCovered >= 10 && timeSpent >= 60 -> 90 // High endurance
        distanceCovered >= 5 && timeSpent >= 30 -> 70 // Moderate endurance
        else -> 50 // Low endurance
    }
}
