package com.viswa.fitfusion.viewmodel


import OnboardingViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



class OnboardingViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnboardingViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
